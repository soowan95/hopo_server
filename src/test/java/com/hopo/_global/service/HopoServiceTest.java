package com.hopo._global.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class HopoServiceTest {

	@InjectMocks
	private HopoService<TestEntity> hopoService;

	@Mock
	private HopoRepository<TestEntity> hopoRepository;

	@BeforeAll
	public static void setup() {
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Entity
	public static class TestEntity extends Hopo {
		private String name;
		private int age;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TestDto extends HopoDto<TestDto, TestEntity> {
		private Integer id;
		private String name;
		private int age;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DeleteTestDto extends HopoDto<TestDto, TestEntity> {
		private Integer id;
	}

	@Test
	@DisplayName("단건 정보 조회")
	public void show_sholdReturnEntity() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		// Given
		TestEntity testEntity = new TestEntity("김수완", 30);
		TestDto testDto = new TestDto(1, "김수완", 30);
		when(hopoRepository.findByParam("id", 1)).thenReturn(Optional.of(testEntity));

		// When
		Object thisEntity = hopoService.show(testDto, null, "test");

		// Then
		assertThat(thisEntity).isNotNull();
	}

	@Test
	@DisplayName("데이터 삭제")
	public void delete_shouldDeleteEntity() {
		// Given
		DeleteTestDto deleteRequest = new DeleteTestDto(1);

		// When
		boolean isDeleted = hopoService.delete(deleteRequest, "test");

		//Then
		assertThat(isDeleted).isTrue();
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복")
	public void checkDuplicate_shouldThrowException_whenDuplicate() {
		TestEntity testEntity = new TestEntity("김수완", 30);
		when(hopoRepository.findByParam("id", 1)).thenReturn(Optional.of(testEntity));

		// Then
		assertThatThrownBy(() -> hopoService.checkDuplicate("id", 1, "test"))
			.isInstanceOf(HttpCodeHandleException.class)
			.satisfies(e -> {
				HttpCodeHandleException httpCodeHandleException = (HttpCodeHandleException) e;
				assertThat(httpCodeHandleException.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
				assertThat(httpCodeHandleException.getMsg()).isEqualTo("이미 사용 중인 아이디입니다.");
			});

		// Verify
		verify(hopoRepository).findByParam("id", 1);
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복 아님")
	public void checkDuplicate_shouldReturnTrue() {
		// When
		when(hopoRepository.findByParam("id", 2)).thenReturn(Optional.empty());

		Boolean result = hopoService.checkDuplicate("id", 2, "test");

		// Then
		assertThat(result).isTrue();

		// Verify
		verify(hopoRepository).findByParam("id", 2);
	}
}
