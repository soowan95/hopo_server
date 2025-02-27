package com.hopo._config.registry;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hopo._utils.HopoStringUtils;
import com.querydsl.core.types.dsl.EntityPathBase;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QClassRegistry {

	private String getPackagePath(String className) {
		return "com.hopo.className.entity.QclassName".replaceAll("className", className);
	}

	public EntityPathBase<?> getQClass(String className) {
		try {
			Class<?> qClass = Class.forName(getPackagePath(className));

			Constructor<?> constructor = qClass.getConstructor(String.class);
			return (EntityPathBase<?>) constructor.newInstance(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}