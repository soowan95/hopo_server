package com.hopo.config.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hopo.user.dto.response.TokenInfoResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

	private static final String AUTHORITIES_KEY = "auth";

	private final String secret;
	private final long accessTokenValidityInMilliseconds;
	private final long refreshTokenValidityInMilliseconds;
	private Key key;

	public TokenProvider(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliseconds,
		@Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds
	) {
		this.secret = secret;
		this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
		this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
	}

	// secret 값을 Base64로 Decode 해서 key 변수에 할당
	@Override
	public void afterPropertiesSet() {

		System.out.println("afterPropertiesSet");

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// Autentication 객체의 권한 정보를 이용해서 토큰을 생성
	public TokenInfoResponse createToken(Authentication authentication) {

		System.out.println("createToken");

		// authorities 설정
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		// 토큰 만료 시간 설정
		long now = (new Date()).getTime();
		Date accessValidity = new Date(now + this.accessTokenValidityInMilliseconds);
		Date refreshValidity = new Date(now + this.refreshTokenValidityInMilliseconds);

		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(accessValidity)
			.compact();

		String refreshToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(refreshValidity)
			.compact();

		return TokenInfoResponse.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public String refreshToken(UserDetails userDetails) {

		Date validity = new Date((new Date()).getTime() + this.accessTokenValidityInMilliseconds);

		return Jwts.builder()
			.setSubject(userDetails.getUsername())
			.claim(AUTHORITIES_KEY, userDetails.getAuthorities())
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(validity)
			.compact();
	}

	public TokenInfoResponse oauthToken(UserDetails userDetails) {

		System.out.println("oauthToken");

		long now = (new Date()).getTime();
		Date accessValidity = new Date(now + this.accessTokenValidityInMilliseconds);
		Date refreshValidity = new Date(now + this.refreshTokenValidityInMilliseconds);

		String accessToken = Jwts.builder()
			.setSubject(userDetails.getUsername())
			.claim(AUTHORITIES_KEY, userDetails.getAuthorities())
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(accessValidity)
			.compact();

		String refreshToken = Jwts.builder()
			.setSubject(userDetails.getUsername())
			.claim(AUTHORITIES_KEY, userDetails.getAuthorities())
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(refreshValidity)
			.compact();

		return TokenInfoResponse.builder()
			.grantType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	// 토큰에 담겨있는 정보를 이용해 Authentication 객체 리턴
	public Authentication getAuthentication(String token) {

		System.out.println("getAuthentication");

		// 토큰을 이용하여 claim 생성
		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		// claim을 이용하여 authorities 생성
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// claim과 authorities 이용하여 User 객체 생성
		User principal = new User(claims.getSubject(), "", authorities);

		// 최종적으로 Authentication 객체 리턴
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	// 토큰의 유효성 검증 수행
	public boolean validateToken(String token) {

		System.out.println("validateToken");

		// 토큰 파싱 후 발생하는 예외 캐치
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 토큰 서명");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않은 JWT 토큰");
		} catch (IllegalArgumentException e) {
			logger.info("잘못된 JWT 토큰");
		}
		return false;
	}
}
