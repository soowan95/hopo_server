package com.hopo._global.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import com.hopo._config.registry.EntityRegistry;
import com.hopo._config.registry.RepositoryRegistry;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class HopoServiceTest {

	@InjectMocks
	private HopoService<TestEntity> hopoService;

	@Mock
	private RepositoryRegistry repositoryRegistry;

	@Mock
	private EntityRegistry entityRegistry;

	@Mock
	private TestRepository testRepository;

	@BeforeAll
	public static void setup() {
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Entity
	public static class TestEntity extends Hopo {
		private Long id;
		private String name;
		private int age;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class TestDto extends HopoDto<TestDto, TestEntity> {
		private Long id;
		private String name;
		private int age;
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DeleteTestDto extends HopoDto<TestDto, TestEntity> {
		private Long id;
	}

	public interface TestRepository extends HopoRepository<TestEntity>, JpaRepository<TestEntity, Integer> {
		void deleteById(Long id);
	}

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SaveTestRequest extends HopoDto<SaveTestRequest, TestEntity> {
		private String name;
		private Integer age;
	}

	@Test
	@DisplayName("entity 저장")
	void save_shouldSaveMethodOfJpaRepository() throws Exception {
		// Given
		SaveTestRequest request = spy(new SaveTestRequest("박수희", 29));
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(entityRegistry.getEntity("test")).thenReturn(new TestEntity());

		// When
		hopoService.save(request, "test");

		// Then
		assertThatCode(() -> verify(repositoryRegistry, only()).getRepository("test")).doesNotThrowAnyException();
		assertThatCode(() -> verify(entityRegistry, only()).getEntity("test")).doesNotThrowAnyException();
		verify(request, times(1)).map(request);
	}

	@Test
	@DisplayName("단건 정보 조회")
	void show_sholdReturnEntity() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		// Given
		TestEntity testEntity = new TestEntity(1L, "김수완", 30);
		TestDto testDto = new TestDto(1L, "김수완", 30);
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(testRepository.findByParam("id", 1L)).thenReturn(Optional.of(testEntity));

		// When
		Object thisEntity = hopoService.show(testDto, "", "test");

		// Then
		assertThat(thisEntity).isNotNull();
	}

	@Test
	@DisplayName("모든 정보 조회")
	void showAll_shouldReturnAllEntities() {
		// Given
		TestEntity testEntity1 = new TestEntity(1L, "김수완", 30);
		TestEntity testEntity2 = new TestEntity(2L, "박수희", 29);
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(testRepository.findAll()).thenReturn(List.of(testEntity1, testEntity2));

		// When
		List<TestEntity> allEntities = hopoService.showAll("test");

		// Then
		assertThat(allEntities).isNotNull();
		assertThat(allEntities.size()).isEqualTo(2);
		assertThat(allEntities.get(0)).isEqualTo(testEntity1);
		assertThat(allEntities.get(1)).isEqualTo(testEntity2);
	}
	@Test
	@DisplayName("update")
	void update_shouldUpdateMethodOfJpaRepository() {
		// Given
		TestDto request = spy(new TestDto(1L, "김수완", 30));
		TestEntity entity = new TestEntity(1L, "박수희", 29);
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(testRepository.findByParam("id", 1L)).thenReturn(Optional.of(entity));

		// When
		hopoService.update(request, "test");

		// Then
		verify(request).map(entity, request);
	}

	@Test
	@DisplayName("데이터 삭제")
	void delete_shouldDeleteEntity() {
		// Given
		DeleteTestDto deleteRequest = new DeleteTestDto(1L);
		TestEntity entity = new TestEntity(1L, "김수완", 30);
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(testRepository.findByParam("id", 1L)).thenReturn(Optional.of(entity));

		// When
		boolean isDeleted = hopoService.delete(deleteRequest, "test");

		//Then
		assertThat(isDeleted).isTrue();
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복")
	void checkDuplicate_shouldThrowException_whenDuplicate() {
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);
		when(testRepository.findByParam("id", 1L)).thenReturn(Optional.of(new TestEntity(1L, "김수완", 30)));

		// Then
		assertThatThrownBy(() -> hopoService.checkDuplicate("id", 1L, "test"))
			.isInstanceOf(HttpCodeHandleException.class)
			.satisfies(e -> {
				HttpCodeHandleException httpCodeHandleException = (HttpCodeHandleException) e;
				assertThat(httpCodeHandleException.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
				assertThat(httpCodeHandleException.getMsg()).isEqualTo("이미 사용 중인 아이디입니다.");
			});
	}

	@Test
	@DisplayName("프로퍼티 명을 통한 중복 확인: 중복 아님")
	void checkDuplicate_shouldReturnTrue() {
		// When
		when(repositoryRegistry.getRepository("test")).thenReturn(testRepository);

		Boolean result = hopoService.checkDuplicate("id", 2L, "test");

		// Then
		assertThat(result).isTrue();
	}
}
