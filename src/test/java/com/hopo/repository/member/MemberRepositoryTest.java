package com.hopo.repository.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hopo._config.annotation.RepositoryTest;
import com.hopo.member.entity.Member;
import com.hopo.member.entity.Role;
import com.hopo.member.entity.Status;
import com.hopo.member.repository.MemberRepository;

@RepositoryTest
public class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("사용자 추가")
	void addUser() {
		// Given
		Member member = member();

		// When
		Member saveMember = memberRepository.save(member);

		// Then
		assertThat(saveMember.getId()).isEqualTo(member.getId());
		assertThat(saveMember.getPassword()).isEqualTo(member.getPassword());
		assertThat(saveMember.getName()).isEqualTo(member.getName());
		assertThat(saveMember.getEmail()).isEqualTo(member.getEmail());
		assertThat(saveMember.getEmailVerified()).isTrue();
		assertThat(saveMember.getStatus()).isEqualTo(Status.BELONG);
		assertThat(saveMember.getRole()).isEqualTo(Role.MEMBER);
		assertThat(saveMember.getUpdatedDate()).isEqualTo(member.getUpdatedDate());
		assertThat(saveMember.getCreatedDate()).isEqualTo(member.getCreatedDate());
		assertThat(saveMember.getDateToFrozen()).isNull();
		assertThat(saveMember.getClosedDate()).isNull();
	}

	@Test
	@DisplayName("마스킹 된 사용자 이름")
	void getMaskedName() {

	}

	private Member member() {
		return Member.builder()
			.id("test")
			.password("pw")
			.name("test")
			.email("test@test.com")
			.emailVerified(true)
			.build();
	}
}
