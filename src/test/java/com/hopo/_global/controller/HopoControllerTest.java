package com.hopo._global.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.google.gson.Gson;
import com.hopo._config.annotation.ControllerTest;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._global._config.controller.TestConfig;
import com.hopo._global.entity.Hopo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ActiveProfiles("p1")
@WebMvcTest(HopoController.class)
@ContextConfiguration(classes = HopoController.class)
@ControllerTest
@ComponentScan(basePackages = "com.hopo._global.controller")
@Import(TestConfig.class)
public class HopoControllerTest {

	@SpyBean
	private DtoRegistry dtoRegistry;

	@Autowired
	private MockMvc mockMvc;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TestEntity extends Hopo {
		private Integer id;
		private String name;
		private boolean isActive;
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("save")
	void save_shouldCallTestServiceImplAndSave() throws Exception {
		// Given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("id", "1");
		params.add("name", "김수완");
		params.add("isActive", "true");

		when(dtoRegistry.getDto("test", "save")).thenCallRealMethod();

		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/test/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(params))
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

		TestEntity entity = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), TestEntity.class);
		assertThat(entity.getId()).isEqualTo(1);
		assertThat(entity.getName()).isEqualTo("김수완");
		assertThat(entity.isActive()).isTrue();
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
