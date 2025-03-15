package com.hopo._config.registry;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityRegistry {

	private String getPackagePath(String entityName) {
		return "com.hopo.entityName.entity.className"
			.replace("entityName", entityName)
			.replace("className", HopoStringUtils.capitalize(entityName));
	}

	public Hopo getEntity(String entityName) {
		try {
			String path = getPackagePath(entityName);
			Class<?> entityClass = Class.forName(path);
			return (Hopo)entityClass
				.getDeclaredConstructor()
				.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.error("{} entity not found. \nmsg: {}", entityName, e.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_ENTITY");
		}
	}
}
