package com.hopo._config;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

	private final EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

	// HopoRepository에서 사용할 entityPath를 bean에 등록하기 위한 설정
	@Bean
	public PathBuilder<?> pathBuilder() {
		// entityManager의 metamodel에서 entity들을 가져옴
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		if (!entities.isEmpty()) {
			// entity가 하나 이상 존재한다면 첫번째 entity를 return
			EntityType<?> entityType = entities.iterator().next();
			Class<?> entityClass = entityType.getJavaType();
			return new PathBuilder<>(entityClass, "e");
		} else {
			throw new IllegalStateException("엔터티가 존재하지 않습니다.");
		}
	}
}
