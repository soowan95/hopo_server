package com.hopo.repository.space;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hopo._config.annotation.RepositoryTest;
import com.hopo.belong.entity.Belong;
import com.hopo.member.entity.Member;
import com.hopo.space.entity.Space;
import com.hopo.space.repository.SpaceRepository;

@RepositoryTest
public class SpaceRepositoryTest {

	@Autowired
	private SpaceRepository spaceRepository;

	@Test
	@DisplayName("공간 생성")
	void addSpace() {
		// Given
		Member member = member();
		Belong belong = belong();
		Space space = Space.builder().belong(belong).member(member).build();

		// When
		Space saveSpace = spaceRepository.save(space);

		// Then
		assertThat(saveSpace.getBelong()).isEqualTo(belong);
		assertThat(saveSpace.getMember()).isEqualTo(member);
		assertThat(saveSpace.getIsOwner()).isFalse();
	}

	@Test
	@DisplayName("소속으로 구성원 찾기")
	void findMemberByBelong() {
		// Given
		Member member1 = member();
		Member member2 = member2();
		Belong belong = belong();

		Space space1 = Space.builder().member(member1).belong(belong).build();
		Space space2 = Space.builder().member(member2).belong(belong).build();

		Space saveSpace1 = spaceRepository.save(space1);
		Space saveSpace2 = spaceRepository.save(space2);

		// When
		List<Space> spaces = spaceRepository.findAllByBelong(belong);

		// Then
		assertThat(spaces.size()).isEqualTo(2);
		assertThat(spaces.get(0).getMember()).isEqualTo(saveSpace1.getMember());
		assertThat(spaces.get(1).getMember()).isEqualTo(saveSpace2.getMember());
	}

	private Member member() {
		return Member.builder()
			.loginId("test")
			.email("test")
			.password("test")
			.name("test")
			.build();
	}

	private Member member2() {
		return Member.builder()
			.loginId("test2")
			.email("test2")
			.password("test2")
			.name("test2")
			.build();
	}

	private Belong belong() {
		return Belong.builder()
			.code("test1234sw")
			.address("test")
			.isCompany(false)
			.build();
	}
}
