package com.hopo.controller.belong;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.hopo._config.annotation.ControllerTest;
import com.hopo.belong.controller.BelongController;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.dto.response.FamilyNameResponse;
import com.hopo.belong.entity.Belong;
import com.hopo.belong.repository.BelongRepository;
import com.hopo.belong.service.BelongServiceImpl;
import com.hopo.member.entity.Member;
import com.hopo.member.repository.MemberRepository;
import com.hopo.member.service.MemberServiceImpl;
import com.hopo.space.entity.Space;
import com.hopo.space.repository.SpaceRepository;

@ActiveProfiles("p1")
@WebMvcTest(BelongController.class)
@ContextConfiguration(classes = BelongController.class)
@ControllerTest
class BelongControllerTest {

	@SpyBean
	private BelongServiceImpl belongService;

	@SpyBean
	private MemberServiceImpl memberService;

	@MockBean
	private BelongRepository belongRepository;

	@MockBean
	private MemberRepository memberRepository;

	@MockBean
	private SpaceRepository spaceRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("소속 코드 생성")
	void makeBelongCode() throws Exception {
		// Given
		String request = "test";

		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/belong/make_code")
				.queryParam("address", request)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

		CodeResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), CodeResponse.class);
		assertThat(response.getCode().length()).isEqualTo(10);
		assertThat(response.getCode().matches("^[A-Za-z0-9!@#$%=]+$")).isTrue();
		System.out.println(response.getCode());
	}

	@Test
	@DisplayName("주소 확인 - return 가족 구성원")
	void checkMemberName() throws Exception {
		// Given
		String request = "test";
		Belong belong = Belong.builder().address("test").code("someCode").isCompany(false).build();
		Member member1 = Member.builder().name("test").password("1234").email("a@a.a").loginId("test").build();
		Member member2 = Member.builder().name("soowan").password("1234").email("a@a.a").loginId("soowan").build();
		Member member3 = Member.builder().name("any").password("1234").email("a@a.a").loginId("any").build();
		Space space1 = Space.builder().belong(belong).member(member1).isOwner(true).build();
		Space space2 = Space.builder().belong(belong).member(member2).isOwner(true).build();
		Space space3 = Space.builder().belong(belong).member(member3).isOwner(true).build();

		// When
		when(belongRepository.findByParam("address", "test")).thenReturn(Optional.ofNullable(belong));
		belong.setSpaces(List.of(space1, space2, space3));

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/belong/is_family")
				.queryParam("address", request)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

		FamilyNameResponse response = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), FamilyNameResponse.class);

		assertThat(response.getFamilyNames().size()).isEqualTo(3);
		assertThat(response.getFamilyNames().get(0)).isEqualTo("t*s*");
		assertThat(response.getFamilyNames().get(1)).isEqualTo("s*o*a*");
		assertThat(response.getFamilyNames().get(2)).isEqualTo("a*y");
	}
}
