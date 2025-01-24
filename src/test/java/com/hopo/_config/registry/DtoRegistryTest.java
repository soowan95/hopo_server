package com.hopo._config.registry;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;

import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class DtoRegistryTest {

	@InjectMocks
	private DtoRegistry dtoRegistry;

	@Mock
	private List<HopoDto> dtoList;

	@BeforeEach
	public void setUp() {
		dtoList = List.of(new TestRequest());

		dtoRegistry = new DtoRegistry(dtoList);
	}

	private static class TestEntity extends Hopo {	}

	@NoArgsConstructor
	private static class TestRequest extends HopoDto<TestRequest, TestEntity> {	}

	@Test
	@DisplayName("동적으로 Request 가져오기")
	void getDto_shouldReturnTestRequest() {
		// When
		TestRequest request = (TestRequest) dtoRegistry.getDto("Test", null);

		// Then
		assertThat(request).isNotNull();
		assertThat(request.getClass().getSimpleName()).isEqualTo("TestRequest");
	}
}
