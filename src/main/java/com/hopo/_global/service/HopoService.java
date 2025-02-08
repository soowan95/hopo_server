package com.hopo._global.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;

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
			Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
			Method saveMethod = repositoryClass.getMethod("save", Hopo.class);
			return (E)saveMethod.invoke(repositoryClass, request.map(request));
		} catch (Exception e) {
			log.error("Exception: {} \n Message: {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
		}
	}

	/**
	 * 단건 조회
	 * @param request {@link HopoDto HopoDto} column 명
	 * @return entity
	 */
	public E show(HopoDto request, String fieldName, String entityName) throws
		InvocationTargetException,
		NoSuchMethodException,
		IllegalAccessException {
		Object[] args;
		if (fieldName == null)
			args = request.get(0);
		else
			args = new Object[]{fieldName ,request.get(fieldName)};
		Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
		try {
			Method findByParamMethod = repositoryClass.getMethod("findByParam", String.class, Object.class);
			return (E)findByParamMethod.invoke(repositoryClass, args[0].toString(), args[1]);
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
		Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
		try {
			Method findAllMethod = repositoryClass.getMethod("findAll");
			return (List<E>)findAllMethod.invoke(repositoryClass);
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
		Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
		try {
			E entity = show(request, null, entityName);
			request.map(entity, request);
			Method updateMethod = repositoryClass.getMethod("save", Hopo.class);
			return (E)updateMethod.invoke(repositoryClass, entity);
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
		Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
		try {
			E entity = show(request, null, entityName);
			Method deleteByIdMethod = repositoryClass.getMethod("deleteById", Long.class);
			deleteByIdMethod.invoke(repositoryClass, entity.getId());
			return true;
		} catch (Exception e) {
			log.error("데이터 삭제 중 문제가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 삭제 중 문제가 발생했습니다.");
		}
	}

	/**
	 * 중복된 데이터가 있는지 확인한다.
	 * @param field {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Boolean
	 */
	public Boolean checkDuplicate(String field, Object v, String entityName) {
		Class<?> repositoryClass = repositoryRegistry.getRepository(entityName).getClass();
		String exceptionCode = switch (field) {
			case "code" -> "DUPLICATE_CODE";
			case "id" -> "DUPLICATE_ID";
			default -> "NO_SUCH_FIELD";
		};
		try {
			Method findByParamMethod = repositoryClass.getMethod("findByParam", String.class, Object.class);
			if (findByParamMethod.invoke(repositoryClass, field, v) == null)
				throw new Exception();
		} catch (Exception e) {
			throw new HttpCodeHandleException(exceptionCode);
		}
		return true;
	}
}
