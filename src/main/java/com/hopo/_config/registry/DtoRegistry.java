package com.hopo._config.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DtoRegistry {

	private final Map<String, HopoDto> dtoMap;

	public DtoRegistry(List<HopoDto> dtoList) {
		this.dtoMap = dtoList.stream()
			.filter(dto -> dto.getClass().getSimpleName().endsWith("Request"))
			.collect(Collectors.toMap(dto -> dto.getClass().getSimpleName(), dto -> dto));
	}

	private String buildDtoName(String entityName, String methodName) {
		return HopoStringUtils.capitalize(methodName) + HopoStringUtils.capitalize(entityName) + "Request";
	}

	public HopoDto getDto(String entityName, String methodName) {
		HopoDto request = dtoMap.get(buildDtoName(entityName, methodName));
		if (request == null) {
			log.error("{} dto not found", buildDtoName(entityName, methodName));
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		}
		return request;
	}
}
