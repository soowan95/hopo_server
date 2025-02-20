package com.hopo.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hopo._config.registry.EntityRegistry;
import com.hopo._config.registry.RepositoryRegistry;
import com.hopo._global.service.HopoService;
import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.dto.response.MemberResponse;
import com.hopo.member.entity.Member;
import com.hopo.member.repository.MemberRepository;

@Service
@Transactional
public class MemberServiceImpl extends HopoService<Member> implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder bCryptPasswordEncoder;

	public MemberServiceImpl(RepositoryRegistry repositoryRegistry, EntityRegistry entityRegistry, MemberRepository memberRepository,
		PasswordEncoder bCryptPasswordEncoder) {
		super(repositoryRegistry, entityRegistry);
		this.memberRepository = memberRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public MemberResponse signUp(SignUpRequest request) {
		Member member = Member.builder()
			.loginId(request.getLoginId())
			.password(request.getPassword())
			.name(request.getName())
			.email(request.getEmail())
			.build();

		return new MemberResponse().of(memberRepository.save(member.hashPassword(bCryptPasswordEncoder)));
	}
}
