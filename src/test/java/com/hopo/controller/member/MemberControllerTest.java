package com.hopo.controller.member;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.hopo._config.annotation.ControllerTest;
import com.hopo.belong.service.BelongServiceImpl;
import com.hopo.member.controller.MemberController;
import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.service.MemberServiceImpl;

@ControllerTest
@ActiveProfiles("p1")
@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = MemberController.class)
public class MemberControllerTest {

	@MockBean
	private MemberServiceImpl userService;

	@MockBean
	private BelongServiceImpl belongService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("회원 가입 성공")
	void successSignUp() throws Exception {
		// Given
		SignUpRequest request = signUpRequest();

		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/user/sign_up")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request))
		);

		// Then
		resultActions.andExpect(status().isOk());
	}

	private SignUpRequest signUpRequest() {
		return SignUpRequest.builder()
			.id("test")
			.password("test")
			.name("test")
			.email("test@test.com")
			.belongCode("test")
			.address("test")
			.build();
	}
}
