package com.hopo.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hopo._global.service.HopoService;
import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.dto.response.MemberResponse;
import com.hopo.member.entity.Member;
import com.hopo.member.repository.MemberRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl extends HopoService<Member> implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder bCryptPasswordEncoder;

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
