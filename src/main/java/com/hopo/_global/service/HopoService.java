package com.hopo._global.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(force = true)
public class HopoService<E, ID> {

	private final HopoRepository<E, ID> repository;

	public HopoService(HopoRepository<E, ID> repository) {
		this.repository = repository;
	}

	/**
	 * 엔터티 저장
	 * @param request {@link HopoDto HopoDto} 를 상속받은 Class
	 * @return boolean
	 * @param <D> {@link HopoDto HopoDto} 를 상속받은 Class
	 */
	public <D> boolean save(D request) {
		try {
			// HopoDto 타입인지 확인
			if (request instanceof HopoDto<?, ?> dto) {
				// map 메서드 가져오기
				Method mapMethod = dto.getClass().getDeclaredMethod("map", dto.getClass());
				// map 메서드 실행
				E entity = (E) mapMethod.invoke(dto, dto);

				// 엔터티 저장
				repository.save(entity);
				return true;
			}
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException("데이터 저장에 실패했습니다.", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw new RuntimeException("데이터 저장 중 예외 발생: " + cause.getMessage(), cause);
		}
		return false;
	}


	/**
	 * 단건 조회
	 * @param property {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return entity
	 */
	public E show(String property, Object v) {
		return repository.findByParam(property, v)
			.orElseThrow(() -> new HttpCodeHandleException("NO_SUCH_DATA"));
	}

	/**
	 * 중복된 데이터가 있는지 확인한다.
	 * @param property {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Boolean
	 */
	public Boolean checkDuplicate(String property, Object v) {
		String exceptionCode = switch (property) {
			case "code" -> "DUPLICATE_CODE";
			case "id" -> "DUPLICATE_ID";
			default -> "NO_SUCH_PROPERTY";
		};
		repository.findByParam(property, v).ifPresent(e -> {
			throw new HttpCodeHandleException(exceptionCode);
		});
		return true;
	}
}
