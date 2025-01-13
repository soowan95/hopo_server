package com.hopo._global.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

	@Test
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
}
