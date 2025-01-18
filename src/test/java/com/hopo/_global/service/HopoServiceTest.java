package com.hopo._global.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class HopoServiceTest {

	@InjectMocks
	private HopoService<TestEntity, Integer> hopoService;

	@Mock
	private HopoRepository<TestEntity, Integer> hopoRepository;

	@BeforeAll
	public static void setup() {
	}

	@Data
	@AllArgsConstructor
	@Builder
	public static class TestEntity {
		private Integer id;
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
		private String name;
	}

	@Test
	@DisplayName("요청데이터를 맵핑하여 entity 로 저장")
	public void save_shouldMapRequestToEntityAndSave() {
		// Given
		TestDto testDto = new TestDto(1, "김수완", 30);

		// When
		boolean isSaved = hopoService.save(testDto);

		// Then
		assertThat(isSaved).isTrue();
	}

	@Test
	@DisplayName("단건 정보 조회")
	public void show_sholdReturnEntity() {
		// Given
		TestEntity testEntity = new TestEntity(1, "김수완", 30);
		TestDto testDto = new TestDto(1, "김수완", 30);
		when(hopoRepository.findByParam("id", 1)).thenReturn(Optional.of(testEntity));

		// When
		Object thisEntity = hopoService.show(testDto);

		// Then
		assertThat(thisEntity).isNotNull();
	}

	@Test
	@DisplayName("모든 정보 조회")
	public void showAll_shouldReturnAllEntity() {
		// Given
		when(hopoRepository.findAll()).thenReturn(List.of(new TestEntity(1, "김수완", 30), new TestEntity(2, "박수희", 29)));

		// When
		List<TestEntity> entityList = hopoService.showAll();

		// Then
		assertThat(entityList).isNotNull();
		assertThat(entityList.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("데이터 갱신")
	public void update_shouldUpdateEntity() {
		// Given
		TestEntity testEntity = new TestEntity(1, "김수완", 30);
		TestDto testDto = new TestDto(1, "김수완", 29);
		when(hopoRepository.findByParam("name", "김수완")).thenReturn(Optional.of(testEntity));

		// When
		boolean isUpdated = hopoService.update(testDto);

		// Then
		assertThat(isUpdated).isTrue();
		assertThat(testEntity.age).isEqualTo(29);
	}

	@Test
	@DisplayName("데이터 삭제")
	public void delete_shouldDeleteEntity() {
		// Given
		DeleteTestDto deleteRequest = new DeleteTestDto("김수완");

		// When
		boolean isDeleted = hopoService.delete(deleteRequest);

		//Then
		assertThat(isDeleted).isTrue();
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복")
	public void checkDuplicate_shouldThrowException_whenDuplicate() {
		// // When
		// when(hopoRepository.findByParam("id", someValue)).thenReturn(Optional.of(someEntity1));
		//
		// // Then
		// assertThatThrownBy(() -> hopoService.checkDuplicate("id", someValue))
		// 	.isInstanceOf(HttpCodeHandleException.class)
		// 	.satisfies(e -> {
		// 		HttpCodeHandleException httpCodeHandleException = (HttpCodeHandleException) e;
		// 		assertThat(httpCodeHandleException.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
		// 		assertThat(httpCodeHandleException.getMsg()).isEqualTo("이미 사용 중인 아이디입니다.");
		// 	});
		//
		// // Verify
		// verify(hopoRepository).findByParam("id", someValue);
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복 아님")
	public void checkDuplicate_shouldReturnTrue() {
		// // When
		// when(hopoRepository.findByParam(someField, someValue)).thenReturn(Optional.empty());
		//
		// Boolean result = hopoService.checkDuplicate(someField, someValue);
		//
		// // Then
		// assertThat(result).isTrue();
		//
		// // Verify
		// verify(hopoRepository).findByParam(someField, someValue);
	}
}
