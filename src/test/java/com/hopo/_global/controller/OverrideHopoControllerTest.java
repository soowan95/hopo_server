package com.hopo._global.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hopo._config.annotation.ControllerTest;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.ServiceRegistry;
import com.hopo._global.dto.HopoDto;

@ActiveProfiles("p1")
@SpringBootTest
@ControllerTest
@AutoConfigureMockMvc
public class OverrideHopoControllerTest {

	@MockBean
	OverrideTestController controller;
	@Autowired
	private MockMvc mockMvc;

	@RequestMapping("/api/test")
	static class OverrideTestController extends HopoController {
		public OverrideTestController(ServiceRegistry serviceRegistry,
			DtoRegistry dtoRegistry) {
			super(serviceRegistry, dtoRegistry);
		}

		@PostMapping("/save")
		@Override
		protected ResponseEntity<?> save(String entity, String requestBody) {
			return ResponseEntity.ok(null);
		}

		@GetMapping("/show")
		@Override
		protected ResponseEntity<?> show(String entity, String f, Object v) {
			return ResponseEntity.ok(null);
		}

		@GetMapping("/show/all")
		@Override
		protected ResponseEntity<List<HopoDto>> showAll(String entity) {
			return ResponseEntity.ok(null);
		}

		@PutMapping("/update")
		@Override
		protected ResponseEntity<?> update(String entity, String requestBody) {
			return ResponseEntity.ok(null);
		}

		@DeleteMapping("/delete")
		@Override
		protected ResponseEntity<Void> delete(String entity, Object id) {
			return ResponseEntity.ok().build();
		}
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("call override save")
	void save_shouldCallTestControllerSave() throws Exception {
		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/test/save")
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
		String response = new Gson().toJson(mvcResult.getResponse().getContentAsString());
		assertThat(response).isEqualTo("\"\"");
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("call override show")
	void show_shouldCallTestControllerShow() throws Exception {
		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/show")
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
		String response = new Gson().toJson(mvcResult.getResponse().getContentAsString());
		assertThat(response).isEqualTo("\"\"");
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("call override show all")
	void show_all_shouldCallTestControllerShowAll() throws Exception {
		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/test/show/all")
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
		String response = new Gson().toJson(mvcResult.getResponse().getContentAsString());
		assertThat(response).isEqualTo("\"\"");
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("call override update")
	void update_shouldCallTestControllerUpdate() throws Exception {
		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.put("/api/test/update")
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
		String response = new Gson().toJson(mvcResult.getResponse().getContentAsString());
		assertThat(response).isEqualTo("\"\"");
	}

	@Test
	@WithMockUser(username = "testUser")
	@DisplayName("call override delete")
	void delete_shouldCallTestControllerDelete() throws Exception {
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/test/delete")
		);

		// Then
		resultActions.andExpect(status().isOk());
	}
}
