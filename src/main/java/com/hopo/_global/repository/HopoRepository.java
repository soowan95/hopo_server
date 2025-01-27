package com.hopo._global.repository;

import java.util.Optional;

import com.hopo._global.entity.Hopo;

/**
 * 최상위 Repository Class
 * @param <E> entity
 */
public interface HopoRepository<E extends Hopo> {
	/**
	 * column 과 value 로 entity 를 가져온다
	 * @param field {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Optional - entity
	 */
	Optional<E> findByParam(String field, Object v);
}
