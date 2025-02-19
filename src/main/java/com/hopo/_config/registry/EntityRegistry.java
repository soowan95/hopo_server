package com.hopo._config.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityRegistry {

	private final Map<String, Hopo> entityMap;

	public EntityRegistry(List<Hopo> hopoList) {
		this.entityMap = hopoList.stream()
			.collect(Collectors.toMap(entity -> entity.getClass().getSimpleName(), entity -> entity));
	}

	public Hopo getEntity(String entityName) {
		Hopo entity = entityMap.get(entityName);
		if (entity == null) {
			log.error("{} entity not found", entityName);
			throw new HttpCodeHandleException("NO_SUCH_ENTITY");
		}
		return entity;
	}
}
