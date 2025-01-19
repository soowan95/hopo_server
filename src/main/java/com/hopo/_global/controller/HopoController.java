package com.hopo._global.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;
import com.hopo._utils.HopoStringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/{entity}")
@Tag(name = "최상위 controller", description = "기본 CRUD 호출이 가능한 controller")
@RequiredArgsConstructor
public class HopoController {

	private final ApplicationContext applicationContext;

	private Object getServiceBean(String entity) {
		// Service 구현체 이름 생성
		String serviceName = HopoStringUtils.capitalize(entity) + "ServiceImpl";

		// 해당 이름의 Bean 가져오기
		Object serviceBean = applicationContext.getBean(serviceName);

		if (serviceBean instanceof HopoService<?, ?> service)
			return service;
		else
			throw new HttpCodeHandleException("NO_SUCH_SERVICE");
	}

	@PostMapping("/save")
	@Operation(summary = "저장", description = "데이터 저장")
	public ResponseEntity<?> save(@PathVariable String entity, @RequestBody HopoDto request) {
		HopoService<?, ?> service = (HopoService<?, ?>) getServiceBean(entity);
		return ResponseEntity.ok(service.save(request));
	}
}
