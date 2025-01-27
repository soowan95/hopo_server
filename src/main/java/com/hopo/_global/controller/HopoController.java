package com.hopo._global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.ServiceRegistry;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/{entity}")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Tag(name = "최상위 controller", description = "기본 CRUD 호출이 가능한 controller")
@Slf4j
public class HopoController {

	private final ServiceRegistry serviceRegistry;
	private final DtoRegistry dtoRegistry;

	@PostMapping("/hopo/save")
	@Operation(summary = "저장", description = "데이터 저장")
	protected ResponseEntity<?> save(@PathVariable String entity, @RequestBody String requestBody) {
		try {
			// DTO class 결정
			HopoDto requestPrototype = dtoRegistry.getDto(entity, "save");
			Class<? extends HopoDto> requestClass = requestPrototype.getClass();

			// 요청 본문 DTO 객체로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			HopoDto request = objectMapper.readValue(requestBody, requestClass);

			HopoService service = serviceRegistry.getService(entity);
			return ResponseEntity.ok(service.save(request, entity));
		} catch (Exception e) {
			log.error("데이터 저장 중 오류가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 저장 중 오류가 발생했습니다.");
		}
	}
}
