package com.hopo._global.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.springframework.test.context.ActiveProfiles;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("p1")
public class HopoServiceTest {

	static Object someEntity1;
	static Object someEntity2;
	static Object someEntity3;
	static String someProperty;
	static Object someValue;

	@InjectMocks
	private HopoService<Object, Object> hopoService;

	@Mock
	private HopoRepository<Object, Object> hopoRepository;

	@BeforeAll
	public static void setup() {
		// Given
		someEntity1 = new Object();
		someEntity2 = new Object();
		someEntity3 = new Object();
		someProperty = "someProperty";
		someValue = new Object();
	}

	@Data
	@AllArgsConstructor
	@Builder
	public static class TestEntity {
		private String name;
		private int age;
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	static class TestDto extends HopoDto<TestDto, TestEntity> {
		private String name;
		private int age;

		@Override
		public TestEntity map(TestDto testDto) {
			return new TestEntity(testDto.getName(), testDto.getAge());
		}

		@Override
		public TestEntity map(TestEntity testEntity, TestDto testDto) {
			testEntity.setAge(testDto.getAge());
			return testEntity;
		}

		@Override
		public Object[] get(Integer index) {
			return new Object[] { "name", name };
		}
	}

	@Test
	@DisplayName("요청데이터를 맵핑하여 entity 로 저장")
	public void save_shouldMapRequestToEntityAndSave() {
		// Given
		TestDto testDto = new TestDto("김수완", 30);

		// When
		boolean isSaved = hopoService.save(testDto);

		// Then
		assertThat(isSaved).isTrue();
	}

	@Test
	@DisplayName("단건 정보 조회")
	public void show_sholdReturnEntity() {
		// Given
		when(hopoRepository.findByParam(someProperty, someValue)).thenReturn(Optional.of(someEntity1));

		// When
		Object thisEntity = hopoService.show(someProperty, someValue);

		// Then
		assertThat(thisEntity).isNotNull();
	}

	@Test
	@DisplayName("모든 정보 조회")
	public void showAll_shouldReturnAllEntity() {
		// Given
		when(hopoRepository.findAll()).thenReturn(List.of(someEntity1, someEntity2, someEntity3));

		// When
		List<Object> entityList = hopoService.showAll();

		// Then
		assertThat(entityList).isNotNull();
		assertThat(entityList.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("데이터 갱신")
	public void update_shouldUpdateEntity() {
		// Given
		TestEntity testEntity = new TestEntity("김수완", 30);
		TestDto testDto = new TestDto("김수완", 29);
		when(hopoRepository.findByParam("name", "김수완")).thenReturn(Optional.of(testEntity));

		// When
		boolean isUpdated = hopoService.update(testDto);

		// Then
		assertThat(isUpdated).isTrue();
		assertThat(testEntity.age).isEqualTo(29);
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복")
	public void duplicateValueCheckByPropertyName() {
		// When
		when(hopoRepository.findByParam(someProperty, someValue)).thenReturn(Optional.of(someEntity1));

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
