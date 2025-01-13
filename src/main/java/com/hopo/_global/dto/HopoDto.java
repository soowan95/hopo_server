package com.hopo._global.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 최상위 응답 Class
 * @param <D> dto
 * @param <E> entity
 */
@Slf4j
public class HopoDto<D, E> {

	private final Class<E> entityClass;

	/**
	 * Entity Class 를 가져오기 위한 생성자
	 */
	public HopoDto() {
		this.entityClass = (Class<E>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	/**
	 * entity 를 response 에 맵핑한다
	 * @param e entity
	 * @return reponse
	 */
	public D of(E e) {
		try {
			// D 클래스 타입 가져오기
			Class<D> dtoClass = (Class<D>) this.getClass();

			return (D) doBuild(dtoClass, e);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
			throw new RuntimeException("DTO 로 변환에 실패했습니다.", ex);
		}
	}

	/**
	 * request 를 entity 에 맵핑한다
	 * @param d request
	 * @return entity
	 */
	public E map(D d) {
		try {
			// E 클래스 타입 가져오기
			Class<E> entityClass = getEntityClass();

			return (E) doBuild(entityClass, d);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
			throw new RuntimeException("ENTITY 로 변환에 실패했습니다.", ex);
		}
	}

	/**
	 * builder 메서드를 통해 build
	 * @param classType {@link Class Class}
	 * @param o {@link HopoDto HopoDto} 를 상속받은 객체
	 * @return Object
	 */
	private Object doBuild(Class classType, Object o) throws
		NoSuchMethodException,
		InvocationTargetException,
		IllegalAccessException {
		// 빌더 메서드 호출
		Method builderMethod = classType.getMethod("builder");
		Object builder = builderMethod.invoke(null);

		// 해당 클래스의 빌더 메서드들 호출
		Method[] methods = builder.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String fieldName = method.getName();
			try {
				Method thisMethod = o.getClass().getMethod("get" + HopoStringUtils.capitalize(fieldName));
				Object value = thisMethod.invoke(o);
				method.invoke(builder, value);
			} catch (NoSuchMethodException ignore) {
				// 해당 메서드가 없으면 무시
			}
		}
		// 빌드 메서드 리턴
		Method buildMethod = builder.getClass().getMethod("build");
		return buildMethod.invoke(builder);
	}

	/**
	 * Entity Class getter
	 * @return Class
	 */
	private Class<E> getEntityClass() {
		return entityClass;
	}
}
