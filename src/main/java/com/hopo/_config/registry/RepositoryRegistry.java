package com.hopo._config.registry;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.hopo._global.repository.HopoRepository;
import com.hopo._utils.HopoStringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepositoryRegistry {

	private final ApplicationContext applicationContext;

	private String buildRepositoryName(String entityName) {
		return HopoStringUtils.uncapitalize(entityName) + "Repository";
	}

	public HopoRepository getRepository(String entityName) {
		return applicationContext.getBean(buildRepositoryName(entityName), HopoRepository.class);
	}
}
