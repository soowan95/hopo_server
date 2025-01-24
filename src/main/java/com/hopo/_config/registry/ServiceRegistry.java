package com.hopo._config.registry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceRegistry {
	private final Map<String, HopoService> serviceMap;

	public ServiceRegistry(List<HopoService> serviceList) {
		this.serviceMap = serviceList.stream()
			.collect(Collectors.toMap(service -> service.getClass().getSimpleName(), service -> service));
	}

	private String buildServiceName(String serviceName) {
		return HopoStringUtils.capitalize(serviceName) + "ServiceImpl";
	}

	public HopoService getService(String serviceName) {
		HopoService hopoService = serviceMap.get(buildServiceName(serviceName));
		if (hopoService == null) {
			log.error("{} service not found", buildServiceName(serviceName));
			throw new HttpCodeHandleException("NO_SUCH_SERVICE");
		}
		return hopoService;
	}
}
