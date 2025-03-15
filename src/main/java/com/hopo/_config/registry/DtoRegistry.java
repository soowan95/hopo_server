package com.hopo._config.registry;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DtoRegistry {

	private String getPackagePath(String entityName, String className, String dtoType) {
		return "com.hopo.entityName.dto.dtoType.className"
			.replace("entityName", entityName)
			.replace("dtoType", dtoType)
			.replace("className", className);
	}

	private String buildRequestName(String entityName, String methodName) {
		return HopoStringUtils.capitalize(methodName) + HopoStringUtils.capitalize(entityName) + "Request";
	}

	private String buildResponseName(String entityName, String methodName) {
		return HopoStringUtils.capitalize(methodName) + HopoStringUtils.capitalize(entityName) + "Response";
	}

	public HopoDto getRequest(String entityName, String methodName) {
		try {
			String path = getPackagePath(entityName, buildRequestName(entityName, methodName), "request");
			Class<?> requestClass = Class.forName(path);
			return (HopoDto) requestClass.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			log.error(getErrorMsg(buildRequestName(entityName, methodName), e.getMessage()));
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		}
	}

	public HopoDto getResponse(String entityName, String methodName) {
		try {
			String path = getPackagePath(entityName, buildResponseName(entityName, methodName), "response");
			Class<?> responseClass = Class.forName(path);
			return (HopoDto) responseClass.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.error(getErrorMsg(buildRequestName(entityName, methodName), e.getMessage()));
			throw new HttpCodeHandleException("NO_SUCH_RESPONSE");
		}
	}

	private String getErrorMsg(String className, String errorMessage) {
		return className + " dto not found. \nmsg: " + errorMessage;
	}
}
