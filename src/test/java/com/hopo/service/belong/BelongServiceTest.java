package com.hopo.service.belong;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.hopo._global.exception.CustomException;
import com.hopo.belong.Belong;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.request.UpdateBelongRequest;
import com.hopo.belong.service.BelongServiceImpl;

@SpringBootTest
@ActiveProfiles("p1")
class BelongServiceTest {

	@Autowired
	private BelongServiceImpl belongService;

	@Test
	@DisplayName("소속 코드 생성")
	public void makeBelongCode() throws Exception {
		String code = belongService.makeCode();
		System.out.println("-------------------code-------------------------");
		System.out.println(code);
		assertThat(code).isNotNull();
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
