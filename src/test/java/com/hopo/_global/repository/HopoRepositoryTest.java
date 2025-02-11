package com.hopo._global.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.hopo._global.entity.Hopo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("p1")
public class HopoRepositoryTest {

	@InjectMocks
	private HopoRepositoryImpl<Hopo> hopoRepository;

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private PathBuilder<Object> entityPath;

	@Mock
	private JPAQuery<Object> jpaQuery;

	@Test
	@DisplayName("프로퍼티 명과 값으로 엔터티 찾기")
	public void findByParam_shouldFindByParam() {
		// Given
		Object someEntity = new Object();
		String someField = "someField";
		Object someValue = new Object();

		BooleanExpression booleanExpression = mock(BooleanExpression.class);
		PathBuilder<Object> pathBuilderMock = mock(PathBuilder.class);

		when(queryFactory.selectFrom(entityPath)).thenReturn(jpaQuery);
		when(entityPath.get(someField)).thenReturn(pathBuilderMock);
		when(pathBuilderMock.eq(someValue)).thenReturn(booleanExpression);
		when(jpaQuery.where(booleanExpression)).thenReturn(jpaQuery);
		when(jpaQuery.fetchOne()).thenReturn(someEntity);

		// When
		Optional<Object> result = Optional.ofNullable(hopoRepository.findByParam(someField, someValue));

		// Then
		assertThat(Optional.of(someEntity)).isEqualTo(result);

		// Verify
		verify(queryFactory).selectFrom(entityPath);
		verify(jpaQuery).where(any(BooleanExpression.class));
		verify(jpaQuery).fetchOne();
	}
}