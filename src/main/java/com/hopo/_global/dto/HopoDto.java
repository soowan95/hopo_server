package com.hopo._global.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 최상위 응답 Class
 * @param <D> dto
 * @param <E> entity
 */
@Slf4j
public class HopoDto<D extends HopoDto, E> {

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

			List<String> fieldNames = getFieldNames(dtoClass);
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
	 * @param target {@link String String} field 또는 value
	 * @return Object
	 */
	public Object get(Integer index, String target) {
		if (index == null)
			index = 0;
		return switch (target) {
			case "field" -> get(index)[0];
			case "value" -> get(index)[1];
			default -> get(index);
		};
	}

	/**
	 * index 번째의 field 에 value 를 저장한다.
	 * @param index {@link Integer Integer} default = 0
	 * @param value {@link Object Object}
	 * @return Class
	 */
	public D set(Integer index, Object value) {
		if (index == null)
			index = 0;
		try {
			// 현재 클래스의 타입 가져오기
			Class<D> dtoClass = (Class<D>) this.getClass();

			List<String> fieldNames = getFieldNames(dtoClass);
			List<? extends Class<?>> fieldTypes = getFieldTypes(dtoClass);
			String field = fieldNames.get(index);
			Class<?> fieldType = fieldTypes.get(index);
			String setMethodName = "set" + HopoStringUtils.capitalize(field);

			Method setMethod = dtoClass.getDeclaredMethod(setMethodName, fieldType);
			setMethod.invoke(this, castValue(value, fieldType));
			return (D) this;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			log.error(e.getMessage());
			throw new HttpCodeHandleException(500, e.getMessage());
		}
	}

	/**
	 * 첫번째 field 에 value 를 저장한다.
	 * @param value {@link Object Object}
	 * @return Class
	 */
	public D set(Object value) {
		return set(0, value);
	}

	/**
	 * field 이름을 순서대로 가져온다
	 * @param dtoClass {@link HopoDto HopoDto}
	 * @return List
	 */
	private List<String> getFieldNames(Class<D> dtoClass) {
		// 클래스의 필드 순서 가져오기
		Field[] fields = dtoClass.getDeclaredFields();

		// 필드 이름을 순서대로 배열로 변환
		return Arrays.stream(fields)
			.map(Field::getName)
			.toList();
	}

	/**
	 * field 타입을 순서대로 가져온다
	 * @param dtoClass {@link HopoDto HopoDto}
	 * @return List
	 */
	private List<? extends Class<?>> getFieldTypes(Class<D> dtoClass) {
		// 클래스의 필드 순서 가져오기
		Field[] fields = dtoClass.getDeclaredFields();

		// 필드 타입을 순서대로 배열로 변환
		return Arrays.stream(fields)
			.map(Field::getType)
			.toList();
	}

	/**
	 * Object 를 field 의 Type 으로 형변환
	 * @param value {@link Object Object}
	 * @param fieldType {@link Class Class} field 의 Type
	 * @return Object
	 */
	private Object castValue(Object value, Class<?> fieldType) {
		if (value == null) return null;

		if (fieldType.isAssignableFrom(value.getClass())) return value;

		if (fieldType == Integer.class || fieldType == int.class)
			return Integer.valueOf(value.toString());
		else if (fieldType == Long.class || fieldType == long.class)
			return Long.valueOf(value.toString());
		else if (fieldType == Double.class || fieldType == double.class)
			return Double.valueOf(value.toString());
		else
			return value.toString();
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
			Method thisMethod;
			try {
				if (fieldName.startsWith("is"))
					thisMethod = o.getClass().getMethod(fieldName);
				else
				 thisMethod = o.getClass().getMethod("get" + HopoStringUtils.capitalize(fieldName));
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
