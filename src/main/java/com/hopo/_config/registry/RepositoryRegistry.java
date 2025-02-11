package com.hopo._config.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class RepositoryRegistry {
	private final Map<String, HopoRepository> repositoryMap;

	public RepositoryRegistry(List<HopoRepository> repositoryList) {
		this.repositoryMap = repositoryList.stream()
			.collect(Collectors.toMap(repository -> repository.getClass().getSimpleName(), repository -> repository));
	}

	private String buildRepositoryName(String entityName) {
		return HopoStringUtils.capitalize(entityName) + "Repository";
	}

	public HopoRepository getRepository(String entityName) {
		HopoRepository repository = repositoryMap.get(buildRepositoryName(entityName));
		if (repository == null) {
			log.error("{} repository not found", buildRepositoryName(entityName));
			throw new HttpCodeHandleException("NO_SUCH_REPOSITORY");
		}
		return repository;
	}
}
