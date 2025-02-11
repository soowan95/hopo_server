package com.hopo.service.belong;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hopo._config.annotation.ServiceTest;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.dto.response.SaveBelongResponse;
import com.hopo.belong.entity.Belong;
import com.hopo.belong.repository.BelongRepository;
import com.hopo.belong.service.BelongServiceImpl;
import com.hopo.member.entity.Member;
import com.hopo.space.entity.Space;

@ServiceTest
public class BelongServiceTest {

	@InjectMocks
	private BelongServiceImpl belongService;

	@Mock
	private BelongRepository belongRepository;

	@Test
	@DisplayName("소속 생성: 성공")
	void successAddBelong() {
		// Given
		SaveBelongRequest request = SaveBelongRequest.builder().address("test").code("1234123412").build();
		Belong belong = Belong.builder().code(request.getCode()).address(request.getAddress()).build();

		when(belongRepository.save(belong)).thenReturn(belong);

		// When
		SaveBelongResponse response = belongService.save(request);

		// Then
		assertThat(response.getCode()).isEqualTo("1234123412");
	}

	@Test
	@DisplayName("소속 생성: 실패, 중복된 코드")
	void failAddBelongByDuplicateCode() {
		// Given
		SaveBelongRequest request = SaveBelongRequest.builder().address("test").code("1234123412").build();
		Belong oldBelong = Belong.builder().address("test1").code("1234123412").build();

		when(belongRepository.findByParam("code", request.getCode())).thenReturn(Optional.of(oldBelong));


		// When
		assertThatThrownBy(() -> belongService.save(request)).isInstanceOf(HttpCodeHandleException.class);
	}

	@Test
	@DisplayName("코드 생성: 성공")
	void successMakeCode() {
		// Given
		String address = "test";

		// When
		CodeResponse response = belongService.makeCode(address);

		// Then
		assertThat(response.getCode().length()).isEqualTo(10);
		assertThat(response.getCode().matches("^[A-Za-z0-9!@#$%=]+$")).isTrue();
	}

	@Test
	@DisplayName("코드 생성: 실패, 구성원 확인")
	void failMakeCodeAndCheckCoMember() {
		// Given
		String address = "test";
		Belong belong = Belong.builder().code("1234123412").address("test").build();
		Member member1 = Member.builder().name("test1").email("test1").loginId("test1").password("test1").build();
		Member member2 = Member.builder().name("testtest").email("test2").loginId("test2").password("test2").build();
		Space space1 = Space.builder().member(member1).belong(belong).build();
		Space space2 = Space.builder().member(member2).belong(belong).build();
		belong.setSpaces(List.of(space1, space2));

		when(belongRepository.findByParam("address", address)).thenReturn(Optional.of(belong));

		// When
		CodeResponse response = belongService.makeCode(address);

		// Then
		assertThat(response.getCode()).isEqualTo("구성원이 맞습니까?");
	}

	@Test
	@DisplayName("같은 공간 작업하는 구성원 이름 조회")
	void findCoMemberName() {
		// Given
		String address = "test";
		Belong belong = Belong.builder().code("1234123412").address("test").build();
		Member member1 = Member.builder().name("test1").email("test1").loginId("test1").password("test1").build();
		Member member2 = Member.builder().name("test2").email("test2").loginId("test2").password("test2").build();
		Space space1 = Space.builder().member(member1).belong(belong).build();
		Space space2 = Space.builder().member(member2).belong(belong).build();
		belong.setSpaces(List.of(space1, space2));

		when(belongRepository.findByParam("address", address)).thenReturn(Optional.of(belong));
	}
}
