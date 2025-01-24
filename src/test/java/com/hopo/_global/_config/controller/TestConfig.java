package com.hopo._global._config.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.hopo._global.controller.HopoControllerTest;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.service.HopoService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
public class TestConfig {


	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Component
	private static class SaveTestRequest extends HopoDto<SaveTestRequest, HopoControllerTest.TestEntity> {
		private Integer id;
		private String name;
		private boolean isActive;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	private static class TestResponse extends HopoDto<TestResponse, HopoControllerTest.TestEntity> {
		private Integer id;
		private String name;
		private boolean isActive;
	}

	private static class TestSeviceImpl extends HopoService<HopoControllerTest.TestEntity, Integer> { }
}
