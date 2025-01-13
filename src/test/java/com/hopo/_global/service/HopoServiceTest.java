package com.hopo._global.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("p1")
public class HopoServiceTest {

	static Object someEntity;
	static String someProperty;
	static Object someValue;

	@InjectMocks
	private HopoService<HopoRepository<Object>, Object> hopoService;

	@Mock
	private HopoRepository<Object> hopoRepository;

	@BeforeAll
	public static void setup() {
		// Given
		someEntity = new Object();
		someProperty = "someProperty";
		someValue = new Object();
	}

	@Test
	@DisplayName("단건 정보 조회")
	public void show() {
		// Given
		when(hopoRepository.findByParam(someProperty, someValue)).thenReturn(Optional.of(someEntity));

		// When
		Object thisEntity = hopoService.show(someProperty, someValue);

		// Then
		assertThat(thisEntity).isNotNull();
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복")
	public void duplicateValueCheckByPropertyName() {
		// When
		when(hopoRepository.findByParam(someProperty, someValue)).thenReturn(Optional.of(someEntity));

		// Then
		assertThatThrownBy(() -> hopoService.checkDuplicate(someProperty, someValue))
			.isInstanceOf(HttpCodeHandleException.class)
			.satisfies(e -> {
				HttpCodeHandleException httpCodeHandleException = (HttpCodeHandleException) e;
				assertThat(httpCodeHandleException.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
				assertThat(httpCodeHandleException.getMsg()).isEqualTo("이미 사용 중인 아이디입니다.");
			});

		// Verify
		verify(hopoRepository).findByParam(someProperty, someValue);
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복 아님")
	public void notDuplicateValueCheckByPropertyName() {
		// When
		when(hopoRepository.findByParam(someProperty, someValue)).thenReturn(Optional.empty());

		Boolean result = hopoService.checkDuplicate(someProperty, someValue);

		// Then
		assertThat(result).isTrue();

		// Verify
		verify(hopoRepository).findByParam(someProperty, someValue);
	}
}
