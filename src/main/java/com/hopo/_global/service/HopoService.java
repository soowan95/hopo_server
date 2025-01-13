package com.hopo._global.service;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HopoService<R extends HopoRepository<E>, E> {

	private final R repository;

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
