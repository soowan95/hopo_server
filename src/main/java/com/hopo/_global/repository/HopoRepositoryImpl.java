package com.hopo._global.repository;

import java.util.Optional;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HopoRepositoryImpl<E, K, V> implements HopoRepository<E, K, V>{

	private final JPAQueryFactory queryFactory;
	private final PathBuilder<E> entityPath;

	@Override
	public Optional<E> findByParam(K k, V v) {
		JPAQuery<E> query = queryFactory.selectFrom(entityPath)
			.where(entityPath.get(k.toString()).eq(v));

		return Optional.ofNullable(query.fetchOne());
	}
}
