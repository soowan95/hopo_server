package com.hopo._global._config.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.hopo._global.controller.HopoControllerTest;
import com.hopo._global.dto.HopoDto;

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
}
