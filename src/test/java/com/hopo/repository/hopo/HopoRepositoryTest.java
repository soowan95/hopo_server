package com.hopo.repository.hopo;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.hopo._global.repository.HopoRepositoryImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("p1")
public class HopoRepositoryTest {

	@InjectMocks
	private HopoRepositoryImpl<Object> hopoRepository;

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private PathBuilder<Object> entityPath;

	@Mock
	private JPAQuery<Object> jpaQuery;

	@Test
	@DisplayName("프로퍼티 명과 값으로 엔터티 찾기")
	public void findByPropertyNameAndValue() {
		// Given
		Object someEntity = new Object();
		String someProperty = "someProperty";
		Object someValue = new Object();

		BooleanExpression booleanExpression = mock(BooleanExpression.class);
		PathBuilder<Object> pathBuilderMock = mock(PathBuilder.class);

		when(queryFactory.selectFrom(entityPath)).thenReturn(jpaQuery);
		when(entityPath.get(someProperty)).thenReturn(pathBuilderMock);
		when(pathBuilderMock.eq(someValue)).thenReturn(booleanExpression);
		when(jpaQuery.where(booleanExpression)).thenReturn(jpaQuery);
		when(jpaQuery.fetchOne()).thenReturn(someEntity);

		// When
		Optional<Object> result = hopoRepository.findByParam(someProperty, someValue);

		// Then
		assertThat(Optional.of(someEntity)).isEqualTo(result);

		// Verify
		verify(queryFactory).selectFrom(entityPath);
		verify(jpaQuery).where(any(BooleanExpression.class));
		verify(jpaQuery).fetchOne();
	}
}