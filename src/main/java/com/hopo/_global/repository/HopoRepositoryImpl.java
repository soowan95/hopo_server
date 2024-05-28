package com.hopo._global.repository;

import java.util.Optional;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HopoRepositoryImpl<E, V> implements HopoRepository<E, V>{

	private final JPAQueryFactory queryFactory;
	private final PathBuilder<E> entityPath;

	@Override
	public Optional<E> findByParam(String property, V v) {
		JPAQuery<E> query = queryFactory.selectFrom(entityPath)
			.where(entityPath.get(property).eq(v));

		return Optional.ofNullable(query.fetchOne());
	}
}
