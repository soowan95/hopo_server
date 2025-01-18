package com.hopo._global.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.hopo._global.exception.HttpCodeHandleException;
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
			throw new HttpCodeHandleException(500, "DTO 로 변환에 실패했습니다." + ex.getMessage());
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
			log.error(ex.getMessage());
			throw new HttpCodeHandleException(500, "ENTITY 로 변환에 실패했습니다." + ex.getMessage());
		}
	}

	/**
	 * request 를 기존 entity 에 맵핑한다
	 * @param e entity
	 * @param d request
	 * @return entity
	 */
	public E map(E e, D d) {
		try {
			// d의 필드 가져오기
			Field[] dFields = d.getClass().getDeclaredFields();

			for (Field dField : dFields) {
				dField.setAccessible(true);
				Object value = dField.get(d);

				// e의 필드와 매칭하여 값 설정
				Field eField;
				try {
					eField = e.getClass().getDeclaredField(dField.getName());
					eField.setAccessible(true);
					eField.set(e, value);
				} catch (NoSuchFieldException ex) {
					// e에 해당 필드가 없는 경우 무시
				}
			}

			return e; // 수정된 e 반환
		} catch (IllegalAccessException error) {
			throw new HttpCodeHandleException(500, "맵핑 중 오류 발생" + error.getMessage());
		}
	}


	/**
	 * index 번째의 객체 fieldName, value 를 가져온다
	 * @param index {@link Integer Integer} default = 0
	 * @return Object[]
	 */
	public Object[] get(Integer index) {
		if (index == null)
			index = 0;
		int cnt = 0;
		Object value = null;
		String field;
		try {
			// 현재 클래스의 타입 가져오기
			Class<D> dtoClass = (Class<D>) this.getClass();

			// 클래스의 필드 순서 가져오기
			Field[] fields = dtoClass.getDeclaredFields();

			// 필드 이름을 순서대로 배열로 변환
			List<String> fieldNames = Arrays.stream(fields)
				.map(Field::getName)
				.toList();
			field = fieldNames.get(index);

			// 클래스의 메서드 가져오기
			Method[] methods = dtoClass.getDeclaredMethods();

			// 메서드 정렬: 필드 순서를 기준으로 정렬
			List<Method> sortedMethods = Arrays.stream(methods)
				.filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
				.sorted(Comparator.comparingInt(method -> {
					String fieldName;
					if (method.getName().startsWith("get"))
						fieldName = HopoStringUtils.uncapitalize(method.getName().substring(3));
					else
						fieldName = method.getName();
					return fieldNames.indexOf(fieldName);
				}))
				.toList();

			for (Method method : sortedMethods) {
				if (cnt == index) {
					value = method.invoke(this);
					break;
				}
				cnt++; // 카운터 증가
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			log.error(e.getMessage());
			throw new HttpCodeHandleException(500, e.getMessage());
		}
		return new Object[]{field, value};
	}

	/**
	 * index 번째의 fieldName 또는 value 를 가져은다.
	 * @param index {@link Integer Integer} default = 0
	 * @param args {@link String String} field 또는 value
	 * @return Object
	 */
	public Object get(Integer index, String args) {
		if (index == null)
			index = 0;
		return switch (args) {
			case "field" -> get(index)[0];
			case "value" -> get(index)[1];
			default -> get(index);
		};
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
