package com.hopo._global.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hopo._config.registry.EntityRegistry;
import com.hopo._config.registry.RepositoryRegistry;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 모드 Service 에서 공동으로 사용할 class
 * @param <E> Entity
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HopoService<E extends Hopo> {

	private final RepositoryRegistry repositoryRegistry;
	private final EntityRegistry entityRegistry;

	/**
	 * 엔터티 저장
	 * @param request {@link HopoDto HopoDto}
	 * @return boolean
	 */
	public E save(HopoDto request, String entityName) throws
		InvocationTargetException,
		NoSuchMethodException,
		IllegalAccessException {
		try {
			HopoRepository repository = repositoryRegistry.getRepository(entityName);
			Hopo entity = entityRegistry.getEntity(entityName);
			Method saveMethod = repository.getClass().getMethod("save", entity.getClass());
			return (E)saveMethod.invoke(repository, request.map(request));
		} catch (Exception e) {
			log.error("Exception: {} \n Message: {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 단건 조회
	 * @param request {@link HopoDto HopoDto} 
	 * @param fieldName {@link String String} field 명
	 * @param entityName {@link String String} entity 명
	 * @return entity
	 */
	public E show(HopoDto request, String fieldName, String entityName) throws
		InvocationTargetException,
		NoSuchMethodException,
		IllegalAccessException {
		Object[] args;
		if (fieldName.isEmpty())
			args = request.get(0);
		else
			args = new Object[]{fieldName, request.get(fieldName)};
		Object repository = repositoryRegistry.getRepository(entityName);
		try {
			Method findByParamMethod = repository.getClass().getMethod("findByParam", String.class, Object.class);
			return (E)((Optional<?>) findByParamMethod.invoke(repository, args[0].toString(), args[1])).orElse(null);
		} catch (Exception e) {
			log.error("데이터를 불러오는데 실패했습니다. \n Message: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * 전체 조회
	 * @return List
	 */
	public List<E> showAll(String entityName) {
		Object repository = repositoryRegistry.getRepository(entityName);
		try {
			Method findAllMethod = repository.getClass().getMethod("findAll");
			return (List<E>)findAllMethod.invoke(repository);
		} catch (Exception e) {
			log.error("데이터를 불러오는데 실패했습니다. \n Message: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 업데이트
	 * @param request {@link HopoDto HopoDto} 첫 번째 field 는 PK *
	 * @return boolean
	 */
	public E update(HopoDto request, String entityName) {
		Object repository = repositoryRegistry.getRepository(entityName);
		try {
			E entity = show(request, "", entityName);
			request.map(entity, request);
			Method updateMethod = repository.getClass().getMethod("save", entity.getClass());
			return (E)updateMethod.invoke(repository, entity);
		} catch (Exception e) {
			log.error("데이터 갱신 중 문제가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 갱신 중 문제가 발생했습니다.");
		}
	}

	/**
	 * 삭제
	 * @param request {@link HopoDto HopoDto} 첫 번째 field 는 PK *
	 * @return boolean
	 */
	public boolean delete(HopoDto request, String entityName) {
		Object repository = repositoryRegistry.getRepository(entityName);
		try {
			E entity = show(request, "", entityName);
			Method deleteByIdMethod = repository.getClass().getMethod("deleteById", Long.class);
			deleteByIdMethod.invoke(repository, entity.getId());
			return true;
		} catch (Exception e) {
			log.error("데이터 삭제 중 문제가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 삭제 중 문제가 발생했습니다.");
		}
	}

	/**
	 * 중복된 데이터가 있는지 확인한다.
	 * @param field {@link String String} column 명
	 * @param value {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Boolean
	 */
	public Boolean checkDuplicate(String field, Object value, String entityName) {
		Object repository = repositoryRegistry.getRepository(entityName);
		String exceptionCode = switch (field) {
			case "code" -> "DUPLICATE_CODE";
			case "id" -> "DUPLICATE_ID";
			default -> "NO_SUCH_FIELD";
		};
		try {
			Method findByParamMethod = repository.getClass().getMethod("findByParam", String.class, Object.class);
			if (((Optional<?>) findByParamMethod.invoke(repository, field, value)).isPresent())
				throw new Exception();
		} catch (Exception e) {
			log.error("시스템 오류가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(exceptionCode);
		}
		return true;
	}
}
