package com.hopo._global.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.hopo._config.HopoControllerTestConfig;
import com.hopo._config.annotation.ControllerTest;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.service.HopoService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ActiveProfiles("p1")
@Import(HopoControllerTestConfig.class)
@WebMvcTest(HopoController.class)
@ContextConfiguration(classes = HopoController.class)
@ControllerTest
@SpyBeans({
	@SpyBean(HopoService.class)
})
public class HopoControllerTest {

	@Autowired
	private HopoController hopoController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ApplicationContext applicationContext;

	@BeforeAll
	public static void setup() {
	}


	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	private static class TestEntity extends Hopo {
		private Integer id;
		private String name;
		private boolean isActive;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	private static class TestRequest extends HopoDto<TestRequest, TestEntity> {
		private Integer id;
		private String name;
		private boolean isActive;
	}

	private static class TestSeviceImpl extends HopoService<TestEntity, Integer> { }

	@Test
	@DisplayName("PathVariable 에 명시된 ServiceImpl 클래스 가져오기")
	void getServiceBean_shouldReturnServiceBeanMatchedPathVariable() {
		// When
		Object testServiceBean = hopoController.getServiceBean("test");

		// Then
		assert testServiceBean instanceof HopoService;
	}

	@Test
	@DisplayName("저장")
	void save_shouldReturnSuccess() {
		// When
		// ResultActions resultActions = mockMvc.perform(
		// 	MockMvcRequestBuilders.get("/api/test/save")
		// )
	}
}
