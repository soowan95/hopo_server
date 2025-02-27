package com.hopo._global.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hopo._config.registry.QClassRegistry;
import com.hopo._global.entity.Hopo;
import com.hopo._utils.HopoStringUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 최상위 Repository 구현체 Class
 * @param <E> entity
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class HopoRepositoryImpl<E extends Hopo> implements HopoRepository<E> {

	private final JPAQueryFactory queryFactory;
	private final QClassRegistry qClassRegistry;

	private boolean isValidField(PathBuilder<Object> pathBuilder, String fieldName) {
		return Arrays.stream(pathBuilder.getType().getDeclaredFields())
			.anyMatch(field -> field.getName().equals(fieldName));
	}

	private String makeEntityName(String className) {
		return HopoStringUtils.uncapitalize(className.replace("Repository", ""));
	}

	/**
	 * column 과 data 로 entity 를 가져온다
	 * @param field {@link String String} column 명
	 * @param value {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Optional - entity
	 */
	@Override
	public Optional<E> findByParam(String field, Object value) {
		try {
			String entityName = makeEntityName(this.getClass().getSimpleName());

			EntityPathBase<?> qClass = qClassRegistry.getQClass(entityName);

			PathBuilder<Object> fieldPath = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
			if (!isValidField(fieldPath, field)) return Optional.empty();
			BooleanExpression condition = fieldPath.get(field).eq(value);

			JPAQuery<E> jpaQuery = queryFactory.selectFrom((EntityPathBase<E>) qClass).where(condition);

			return Optional.ofNullable(jpaQuery.fetchOne());
		} catch (Exception e) {
			log.error("데이터를 가져오는 중 오류가 발생했습니다. \n Message: {}", e.getMessage());
			return Optional.empty();
		}
	}
}
