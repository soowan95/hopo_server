package com.hopo._global.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

class HopoDtoTest {

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
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	static class GetTestDto extends HopoDto<TestDto, TestEntity> {
		private String name;
		private int age;
		private boolean isThird;
	}

	@Test
	@DisplayName("entity 를 DTO 로 맵핑한다")
	void of_shouldMapEntityToDto() {
		// Given
		TestEntity entity = new TestEntity("김수완", 30);

		// When
		TestDto response = new TestDto().of(entity);

		// Then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.getAge()).isEqualTo(30);
	}

	@Test
	@DisplayName("DTO 를 entity 로 맵핑한다")
	void map_shouldMapDtoToEntity() {
		// Given
		TestDto request = new TestDto("김수완", 30);

		// When
		TestEntity entity = new TestDto().map(request);

		// Then
		assertThat(entity).isNotNull();
		assertThat(entity.getName()).isEqualTo("김수완");
		assertThat(entity.getAge()).isEqualTo(30);
	}

	static Stream<Arguments> getTestCases() {
		return Stream.of(
			Arguments.of(0, "name", "김수완"),
			Arguments.of(1, "age", 30),
			Arguments.of(2, "isThird", true)
		);
	}

	@ParameterizedTest
	@MethodSource("getTestCases")
	@DisplayName("index 번째 있는 값을 가져온다")
	void get_shouldReturnValueAtIndex(int index, String expectField, Object expectValue) {
		// Given
		GetTestDto request = new GetTestDto("김수완", 30, true);

		// When
		Object[] firstValue = request.get(index);

		// Then
		assertThat(firstValue[0]).isEqualTo(expectField);
		assertThat(firstValue[1]).isEqualTo(expectValue);
	}

	@ParameterizedTest
	@MethodSource("getTestCases")
	@DisplayName("index 번째 있는 값을 fieldName, value 를 구분하여 가져온다")
	void get_shouldReturnFieldOrValueByIndex(int index, String expectField, Object expectValue) {
		// Given
		GetTestDto request = new GetTestDto("김수완", 30, true);

		// When
		Object field = request.get(index, "field");
		Object value = request.get(index, "value");

		// Then
		assertThat(field).isEqualTo(expectField);
		assertThat(value).isEqualTo(expectValue);
	}
}
