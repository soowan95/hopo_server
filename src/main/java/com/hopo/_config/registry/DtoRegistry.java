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
			.collect(Collectors.toMap(dto -> dto.getClass().getSimpleName(), dto -> dto));
	}

	private String buildRequestName(String entityName, String methodName) {
		return HopoStringUtils.capitalize(methodName) + HopoStringUtils.capitalize(entityName) + "Request";
	}

	private String buildResponseName(String entityName, String methodName) {
		return HopoStringUtils.capitalize(methodName) + HopoStringUtils.capitalize(entityName) + "Response";
	}

	public HopoDto getRequest(String entityName, String methodName) {
		HopoDto request = dtoMap.get(buildRequestName(entityName, methodName));
		if (request == null) {
			log.error("{} dto not found", buildRequestName(entityName, methodName));
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		}
		return request;
	}

	public HopoDto getResponse(String entityName, String methodName) {
		HopoDto response = dtoMap.get(buildResponseName(entityName, methodName));
		if (response == null) {
			log.error("{} dto not found", buildResponseName(entityName, methodName));
			throw new HttpCodeHandleException("NO_SUCH_RESPONSE");
		}
		return response;
	}
}
