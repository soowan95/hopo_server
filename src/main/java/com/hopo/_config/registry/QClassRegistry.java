package com.hopo._config.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.EntityPathBase;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QClassRegistry {

	private final Map<String, EntityPathBase> qClassMap;

	public QClassRegistry(List<EntityPathBase> qClassList) {
		this.qClassMap = qClassList.stream()
			.collect(Collectors.toMap(qClass -> qClass.getClass().getSimpleName(), qClass -> qClass));
	}

	public EntityPathBase getQClass(String className) {
		return qClassMap.get(className);
	}
}
