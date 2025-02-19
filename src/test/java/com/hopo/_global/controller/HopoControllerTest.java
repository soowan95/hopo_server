package com.hopo._global.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
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
import com.google.gson.reflect.TypeToken;
import com.hopo._config.annotation.ControllerTest;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.EntityRegistry;
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
		private Long id;
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateTestRequest extends HopoDto<UpdateTestRequest, TestEntity> {
		private Long id;
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeleteTestRequest extends HopoDto<DeleteTestRequest, TestEntity> {
		private Long id;
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

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateTestResponse extends HopoDto<UpdateTestResponse, TestEntity> {
		private String name;
		@SerializedName("active")
		private boolean isActive;
	}

	@Service
	public static class TestServiceImpl extends HopoService<TestEntity> {
		public TestServiceImpl(RepositoryRegistry repositoryRegistry, EntityRegistry entityRegistry) {
			super(repositoryRegistry, entityRegistry);
		}
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("save")
	void save_shouldCallTestServiceImplAndSave() throws Exception {
		// Given
		Map<String, Object> params = Map.of("name", "김수완", "active", false);

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

		SaveTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(),
			SaveTestResponse.class);
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.isActive()).isFalse();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("show")
	void show_shouldCallTestServiceImplAndShow_withFieldAndValue() throws Exception {
		// Given
		when(dtoRegistry.getRequest("test", "show")).thenReturn(new ShowTestRequest());
		when(dtoRegistry.getResponse("test", "show")).thenReturn(new ShowTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.show(any(HopoDto.class), any(String.class), any(String.class))).thenReturn(
			new TestEntity(1L, "김수완", true));

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

		ShowTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(),
			ShowTestResponse.class);
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.isActive()).isTrue();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("show")
	void show_shouldCallTestServiceImplAndShow_withOnlyValue() throws Exception {
		// Given
		when(dtoRegistry.getRequest("test", "show")).thenReturn(new ShowTestRequest());
		when(dtoRegistry.getResponse("test", "show")).thenReturn(new ShowTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.show(any(HopoDto.class), any(String.class), any(String.class))).thenReturn(
			new TestEntity(1L, "김수완", true));

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
				.param("v", "1")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = finalResultActions.andExpect(status().isOk()).andReturn();

		ShowTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(),
			ShowTestResponse.class);
		assertThat(response.getId()).isEqualTo(1);
		assertThat(response.getName()).isEqualTo("김수완");
		assertThat(response.isActive()).isTrue();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("showAll")
	void show_all_shouldCallTestServiceImplAndShowAll() throws Exception {
		// Given
		when(dtoRegistry.getResponse("test", "show")).thenReturn(new ShowTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.showAll("test")).thenReturn(
			List.of(new TestEntity(1L, "김수완", false), new TestEntity(2L, "박수희", true)));

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/show/all")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/show/all"));

		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/hopo/show/all")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = finalResultActions.andExpect(status().isOk()).andReturn();

		List<ShowTestResponse> responseList = new Gson().fromJson(mvcResult.getResponse().getContentAsString(),
			new TypeToken<List<ShowTestResponse>>() {
			}.getType());
		assertThat(responseList.size()).isEqualTo(2);
		assertThat(responseList.get(0).getName()).isEqualTo("김수완");
		assertThat(responseList.get(0).isActive()).isFalse();
		assertThat(responseList.get(1).getName()).isEqualTo("박수희");
		assertThat(responseList.get(1).isActive()).isTrue();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("update")
	void update_shouldCallTestServiceImplAndUpdate() throws Exception {
		// Given
		TestEntity testEntity = new TestEntity(1L, "김수완", false);
		Map<String, Object> params = Map.of("name", "박수희", "active", true);
		when(dtoRegistry.getRequest("test", "update")).thenReturn(new UpdateTestRequest());
		when(dtoRegistry.getResponse("test", "update")).thenReturn(new UpdateTestResponse());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.update(any(HopoDto.class), any(String.class))).thenReturn(testEntity.builder()
				.name("박수희")
				.isActive(true)
			.build());

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.put("/api/test/update")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/update"));

		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.put("/api/test/hopo/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(params))
		);

		// Then
		MvcResult mvcResult = finalResultActions.andExpect(status().isOk()).andReturn();
		UpdateTestResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), UpdateTestResponse.class);
		assertThat(response.getName()).isEqualTo("박수희");
		assertThat(response.isActive()).isTrue();
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("delete")
	void delete_shouldCallTestServiceImplAndDelete_ok() throws Exception {
		// Given
		when(dtoRegistry.getRequest("test", "delete")).thenReturn(new DeleteTestRequest());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.delete(any(HopoDto.class), any(String.class))).thenReturn(true);

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/test/delete")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/delete"));

		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/test/hopo/delete")
				.param("id", "1")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		finalResultActions.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("delete")
	void delete_shouldCallTestServiceImplAndDelete_noContent() throws Exception {
		// Given
		when(dtoRegistry.getRequest("test", "delete")).thenReturn(new DeleteTestRequest());
		when(serviceRegistry.getService("test")).thenReturn(testService);
		when(testService.delete(any(HopoDto.class), any(String.class))).thenReturn(false);

		// When
		ResultActions redirectResultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/test/delete")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		redirectResultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/api/test/hopo/delete"));

		// When
		ResultActions finalResultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/test/hopo/delete")
				.param("id", "1")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		finalResultActions.andExpect(status().isNoContent());
	}
}
