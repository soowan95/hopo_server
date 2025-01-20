package com.hopo._global.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;
import com.hopo._utils.HopoStringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/{entity}")
@Tag(name = "최상위 controller", description = "기본 CRUD 호출이 가능한 controller")
@RequiredArgsConstructor
@Slf4j
public class HopoController {

	private final ApplicationContext applicationContext;

	/**
	 * Service class 를 동적으로 받아온다
	 * @param entity {@link String String} PathVariable 에서 받아온 entity 명
	 * @return Object
	 */
	HopoService getServiceBean(String entity) {
		// Service 구현체 이름 생성
		String serviceName = HopoStringUtils.capitalize(entity) + "ServiceImpl";

		try {
			// 해당 이름의 Bean 가져오기
			return (HopoService)applicationContext.getBean(serviceName);
		} catch (NoSuchBeanDefinitionException nsbde) {
			log.error("Service 객체 '{}' 가 SpringContext 에 정의되어 있지 않습니다. \n Message: {}", serviceName, nsbde.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		} catch (BeansException be) {
			log.error("SpringContext 에서 Service 객체 '{}' 를 생성하는데 실패했습니다. \n Message: {}", serviceName, be.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		}
	}

	/**
	 * DTO 를 동적으로 받아온다
	 * @param entity {@link String String} PathVariable 에서 받아온 entity 명
	 * @param method {@link String String} 실행할 method 명
	 * @return HopoDto
	 */
	private HopoDto getRequest(String entity, String method) {
		// Dto 이름 생성
		String dtoName = HopoStringUtils.capitalize(method) + HopoStringUtils.capitalize(entity) + "Request";

		try {
			// 해당 이름의 Bean 가져오기
			return applicationContext.getBean(dtoName, HopoDto.class);
		} catch (NoSuchBeanDefinitionException nsbde) {
			log.error("Request 객체 '{}' 가 SpringContext 에 정의되어 있지 않습니다. \n Message: {}", dtoName, nsbde.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		} catch (BeanDefinitionStoreException bdse) {
			log.error("Request 객체 '{}' 가 HopoDto 를 상속하고 있지 않습니다. \n Message: {}", dtoName, bdse.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		} catch (BeansException be) {
			log.error("SpringContext 에서 Request 객체 '{}' 를 생성하는데 실패했습니다. \n Message: {}", dtoName, be.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_REQUEST");
		}
	}

	@PostMapping("/save")
	@Operation(summary = "저장", description = "데이터 저장")
	public ResponseEntity<?> save(@PathVariable String entity, @RequestBody String requestBody) {
		try {
			// DTO class 결정
			HopoDto requestPrototype = getRequest(entity, "save");
			Class<? extends HopoDto> requestClass =requestPrototype.getClass();

			// 요청 본문 DTO 객체로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			HopoDto request = objectMapper.readValue(requestBody, requestClass);

			HopoService<?, ?> service = (HopoService<?, ?>)getServiceBean(entity);
			return ResponseEntity.ok(service.save(request));
		} catch (Exception e) {
			log.error("데이터 저장 중 오류가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 저장 중 오류가 발생했습니다.");
		}
	}
}
