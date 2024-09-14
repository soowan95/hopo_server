package com.hopo.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hopo._config.annotation.ServiceTest;
import com.hopo._global.exception.CustomException;
import com.hopo.member.repository.MemberRepository;
import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.dto.response.MemberResponse;
import com.hopo.member.entity.Member;
import com.hopo.member.entity.Role;
import com.hopo.member.entity.Status;
import com.hopo.member.service.MemberServiceImpl;

@ServiceTest
public class MemberServiceTest {

	@InjectMocks
	private MemberServiceImpl memberService;

	@Mock
	private MemberRepository memberRepository;

	@Spy
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	@DisplayName("소속 없는 회원 가입")
	void signUpWithoutBelong() {
		// Given
		SignUpRequest request = signUpRequest();
		String encryptePw = passwordEncoder.encode(request.getPassword());

		// When
		when(memberRepository.save(any(Member.class))).thenReturn(Member.builder()
			.id(request.getId())
			.password(encryptePw)
			.name(request.getName())
			.email(request.getEmail())
			.emailVerified(true)
			.build());

		MemberResponse response = memberService.signUp(request);

		// Then
		assertThat(response.getId()).isEqualTo(request.getId());
		assertThat(passwordEncoder.matches(request.getPassword(), response.getPassword())).isTrue();
		assertThat(response.getName()).isEqualTo(request.getName());
		assertThat(response.getEmail()).isEqualTo(request.getEmail());
		assertThat(response.getStatus()).isEqualTo(Status.BELONG);
		assertThat(response.getRole()).isEqualTo(Role.MEMBER);

		// Verify
		verify(memberRepository, times(1)).save(any(Member.class));
		verify(passwordEncoder, atLeastOnce()).encode(any(String.class));
	}

	private SignUpRequest signUpRequest() {
		return SignUpRequest.builder()
			.id("test")
			.password("test")
			.name("test")
			.email("test@test.com")
			.belongCode("test")
			.address("test")
			.build();
	}

	@Test
	@DisplayName("아이디 중복 확인: 중복")
	void idDuplicate() {
		// Given
		String newMemberId = "test";
		Member mockMember = Member.builder().id("test").build();

		// When
		when(memberRepository.findByParam("id", newMemberId)).thenReturn(Optional.of(mockMember));

		// Then
		assertThatThrownBy(() -> memberService.checkDuplicate("id", newMemberId)).isInstanceOf(CustomException.class);
	}

	@Test
	@DisplayName("아이디 중복 확인: 중복 아님")
	void idNotDuplicate() {
		// Given
		String newMemberId = "test";

		// When
		when(memberRepository.findByParam("id", newMemberId)).thenReturn(Optional.empty());

		Boolean checkDuplicate = memberService.checkDuplicate("id", newMemberId);

		// Then
		assertThat(checkDuplicate).isTrue();
	}
}
