package com.hopo.repository.belong;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hopo._config.annotation.RepositoryTest;
import com.hopo.belong.entity.Belong;
import com.hopo.belong.repository.BelongRepository;

@RepositoryTest
public class BelongRepositoryTest {

	@Autowired
	private BelongRepository belongRepository;

	@Test
	@DisplayName("소속 추가")
	void addBelong() {
		// Given
		Belong belong = belong();

		// When
		Belong saveBelong = belongRepository.save(belong);

		// Then
		assertThat(saveBelong.getCode()).isEqualTo(belong.getCode());
		assertThat(saveBelong.getAddress()).isEqualTo(belong.getAddress());
		assertThat(saveBelong.getIsCompany()).isFalse();
	}

	@Test
	@DisplayName("코드로 찾기: 성공")
	void successFindByCode() {
		// Given
		String code = "test1234sw";
		Belong belong = belong();

		Belong saveBelong = belongRepository.save(belong);

		// When
		Optional<Belong> findBelong = belongRepository.findByCode(code);

		// Then
		assertThat(findBelong.get().getCode()).isEqualTo(saveBelong.getCode());
		assertThat(findBelong.get().getAddress()).isEqualTo(saveBelong.getAddress());
		assertThat(findBelong.get().getIsCompany()).isFalse();
	}

	@Test
	@DisplayName("코드로 찾기: 실패")
	void failFindByCode() {
		// Given
		String code = "testtestte";

		// When
		Optional<Belong> findBelong = belongRepository.findByCode(code);

		// Then
		assertThat(findBelong).isEqualTo(Optional.empty());
	}

	private Belong belong() {
		return Belong.builder()
			.address("test")
			.code("test1234sw")
			.build();
	}
}
