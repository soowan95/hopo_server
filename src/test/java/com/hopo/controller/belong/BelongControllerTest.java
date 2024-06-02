package com.hopo.controller.belong;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.hopo._config.jwt.JwtFilter;
import com.hopo._config.jwt.TokenProvider;
import com.hopo._global.exception.CustomException;
import com.hopo.belong.Belong;
import com.hopo.belong.BelongController;
import com.hopo.belong.dto.request.MakeCodeRequest;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.service.BelongServiceImpl;

@WebMvcTest(BelongController.class)
@ActiveProfiles("p1")
@Import({TokenProvider.class, JwtFilter.class})
class BelongControllerTest {

	@MockBean
	private BelongServiceImpl belongService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("소속 코드 생성")
	void makeBelongCode() throws Exception {
		// Given
		MakeCodeRequest request = makeCodeRequest();

		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/belong/make_code")
				.param("address", request.getAddress())
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

		CodeResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), CodeResponse.class);
		assertThat(response.getCode().length()).isEqualTo(10);
	}

	private MakeCodeRequest makeCodeRequest() {
		return MakeCodeRequest.builder()
			.address("test")
			.build();
	}

	@Test
	@DisplayName("소속 추가 성공")
	public void successBelongInsert() throws Exception {
		// Given
		SaveBelongRequest request = new SaveBelongRequest("1234asdfAS", "서울특별시", false);

		// Then
		belongService.save(request);
		Belong belong = belongService.findByCode(request.getCode());

		// Then
		assertThat(belong).isNotNull();
	}

	@Test
	@DisplayName("소속 조회: 코드로")
	public void successBelongSelectByCode() throws Exception {
		// Given
		String code = "1234asdfAS";

		// When
		Belong belong = belongService.findByCode(code);

		assertThat(belong).isNotNull();
	}

	@Test
	@DisplayName("소속 조회 실패: 존재하지 않는 코드")
	public void failBelongSelectByCodeCaseNotExistCode() throws Exception {
		// Given
		String code = "1234123412";

		// Then
		assertThrows(CustomException.class, () -> belongService.findByCode(code));
	}

	@Test
	@DisplayName("소속 갱신: 주소")
	public void successBelongUpdateAddress() throws Exception{
		// Given
		UpdateBelongRequest request = new UpdateBelongRequest("1234asdfAS", "부산광역시");

		// When
		belongService.update(request);
		Belong belong = belongService.findByCode(request.getCode());

		// Then
		assertThat(belong.getAddress()).isEqualTo("부산광역시");
	}
}
