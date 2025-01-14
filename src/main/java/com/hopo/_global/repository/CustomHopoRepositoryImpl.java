package com.hopo._global.repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * 최상위 Repository 구현체 Class
 * @param <E> entity
 */
@RequiredArgsConstructor
@Transactional
public class CustomHopoRepositoryImpl<E> implements CustomHopoRepository<E> {

	private final JPAQueryFactory queryFactory;
	private final PathBuilder<E> entityPath;

	/**
	 * column 과 data 로 entity 를 가져온다
	 * @param property {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Optional - entity
	 */
	@Override
	public Optional<E> findByParam(String property, Object v) {
		JPAQuery<E> query = queryFactory.selectFrom(entityPath)
			.where(entityPath.get(property).eq(v));

		return Optional.ofNullable(query.fetchOne());
	}
}
