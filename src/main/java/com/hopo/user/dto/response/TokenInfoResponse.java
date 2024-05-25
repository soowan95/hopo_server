package com.hopo.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Token 정보 응답")
public class TokenInfoResponse {

	@Schema(description = "JWT 인증 타입")
	private String grantType;
	@Schema(description = "access token")
	private String accessToken;
	@Schema(description = "refresh token")
	private String refreshToken;

	@Builder
	public TokenInfoResponse(String grantType, String accessToken, String refreshToken) {
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
