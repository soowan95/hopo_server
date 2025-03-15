package com.hopo._config.registry;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;
import com.hopo._utils.HopoStringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceRegistry {
	private final RepositoryRegistry repositoryRegistry;
	private final EntityRegistry entityRegistry;

	private String getPackagePath(String entityName, String className) {
		return "com.hopo.entityName.service.className"
			.replace("entityName", entityName)
			.replace("className", className);
	}

	private String buildServiceName(String entityName) {
		return HopoStringUtils.capitalize(entityName) + "ServiceImpl";
	}

	public HopoService getService(String entityName) {
		try {
			String path = getPackagePath(entityName, buildServiceName(entityName));
			Class<?> serviceClass = Class.forName(path);
			return (HopoService) serviceClass
				.getDeclaredConstructor(RepositoryRegistry.class, EntityRegistry.class)
				.newInstance(repositoryRegistry, entityRegistry);
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			log.error("{} service not found. \nmsg: {}", buildServiceName(entityName), e.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_SERVICE");
		}
	}
}
