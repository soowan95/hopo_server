package com.hopo._config.registry;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hopo._global.entity.Hopo;
import com.hopo._global.repository.HopoRepository;
import com.hopo._global.service.HopoService;

import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class ServiceRegistryTest {

	@InjectMocks
	private ServiceRegistry serviceRegistry;

	@Mock
	private List<HopoService> serviceList;

	@BeforeEach
	public void setUp() {
		serviceList = List.of(new TestServiceImpl());

		serviceRegistry = new ServiceRegistry(serviceList);
	}

	private static class TestEntity extends Hopo {
	}

	private static class TestServiceImpl extends HopoService<TestEntity> {
	}

	private static class TestRepository implements HopoRepository<TestEntity> {
		@Override
		public Optional<TestEntity> findByParam(String field, Object v) {
			return Optional.empty();
		}
	}

	@Test
	@DisplayName("동적으로 Service 가져오기")
	public void getService_shouldReturnTestServiceImpl() {
		// When
		HopoService<?> service = serviceRegistry.getService("test");

		// Then
		assertThat(service).isNotNull();
		assertThat(service.getClass().getSimpleName()).isEqualTo("TestServiceImpl");
	}
}
