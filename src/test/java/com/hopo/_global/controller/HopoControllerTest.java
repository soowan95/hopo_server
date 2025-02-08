package com.hopo._global.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.hopo._config.annotation.ControllerTest;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.RepositoryRegistry;
import com.hopo._config.registry.ServiceRegistry;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.service.HopoService;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ActiveProfiles("p1")
@SpringBootTest
@ControllerTest
@AutoConfigureMockMvc
public class HopoControllerTest {

	@MockBean
	private DtoRegistry dtoRegistry;

	@MockBean
	private ServiceRegistry serviceRegistry;

	@MockBean
	private TestServiceImpl testService;

	@Autowired
	private MockMvc mockMvc;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@Entity
	public static class TestEntity extends Hopo {
		private Long id;
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SaveTestRequest extends HopoDto<SaveTestRequest, TestEntity> {
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ShowTestRequest extends HopoDto<ShowTestRequest, TestEntity> {
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SaveTestResponse extends HopoDto<SaveTestResponse, TestEntity> {
		private Long id;
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ShowTestResponse extends HopoDto<ShowTestResponse, TestEntity> {
		private Long id;
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Service
	public static class TestServiceImpl extends HopoService<TestEntity> {
		public TestServiceImpl(RepositoryRegistry repositoryRegistry) {
			super(repositoryRegistry);
		}
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("save")
	void save_shouldCallTestServiceImplAndSave() throws Exception {
		// Given
		Map<String, Object> params = new HashMap<>();
		params.put("name", "김수완");
		params.put("active", false);

		when(dtoRegistry.getRequest("test", "save")).thenReturn(new SaveTestRequest());
		when(dtoRegistry.getResponse("test", "save")).thenReturn(new SaveTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);

		when(testService.save(any(HopoDto.class), any(String.class))).thenReturn(new TestEntity(1L, "김수완", false));

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/test/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(params))
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/save"));

		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/test/hopo/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(params))
		);

		// Then
		MvcResult mvcResult = finalResultActions.andExpect(status().isOk()).andReturn();

		SaveTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), SaveTestResponse.class);
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.isActive()).isTrue();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("show")
	void show_shouldCallTestServiceImplAndShow_withFieldAndValue() throws Exception {
		// Given
		when(dtoRegistry.getRequest("test", "show")).thenReturn(new ShowTestRequest());
		when(dtoRegistry.getResponse("test", "show")).thenReturn(new ShowTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.show(any(HopoDto.class), any(String.class), any(String.class))).thenReturn(new TestEntity(1L, "김수완", true));

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/show")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/show"));


		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/hopo/show")
				.param("f", "name")
				.param("v", "김수완")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = finalResultActions.andExpect(status().isOk()).andReturn();

		ShowTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ShowTestResponse.class);
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.isActive()).isTrue();
	}
}
