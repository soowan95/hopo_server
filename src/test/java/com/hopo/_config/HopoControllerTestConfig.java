package com.hopo._config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.hopo._global.controller.HopoController;
import com.hopo._global.controller.HopoControllerTest;
import com.hopo._global.service.HopoService;

@TestConfiguration
public class HopoControllerTestConfig {

	public HopoController hopoController(ApplicationContext applicationContext) {
		return new HopoController(applicationContext);
	}
}
