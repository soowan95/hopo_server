package com.hopo._global.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.ServiceRegistry;
import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.service.HopoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/{entity}")
@RequiredArgsConstructor
@Tag(name = "최상위 controller", description = "기본 CRUD 호출이 가능한 controller")
@Slf4j
public class HopoController {

	private final ServiceRegistry serviceRegistry;
	private final DtoRegistry dtoRegistry;

	@PostMapping("/hopo/save")
	@Operation(summary = "저장", description = "데이터 저장")
	protected ResponseEntity<?> save(@PathVariable String entity, @RequestBody String requestBody) {
		try {
			// Request class 결정
			HopoDto requestPrototype = dtoRegistry.getRequest(entity, "save");
			Class<? extends HopoDto> requestClass = requestPrototype.getClass();

			// 요청 본문 DTO 객체로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			HopoDto request = objectMapper.readValue(requestBody, requestClass);

			// Service class 결정
			HopoService service = serviceRegistry.getService(entity);

			// Response class 결정
			HopoDto responsePrototype = dtoRegistry.getResponse(entity, "save");

			return ResponseEntity.ok(responsePrototype.of(service.save(request, entity)));
		} catch (HttpCodeHandleException hhe) {
			throw hhe;
		} catch (Exception e) {
			log.error("데이터 저장 중 오류가 발생했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 저장 중 오류가 발생했습니다.");
		}
	}

	@GetMapping("/hopo/show")
	@Operation(summary = "조회", description = "단건 조회")
	protected ResponseEntity<?> show(@PathVariable String entity, @RequestParam(defaultValue = "") String f, @RequestParam Object v) {
		try {
			// Request class 결정
			HopoDto requestPrototype = dtoRegistry.getRequest(entity, "show");

			// field 이름이 파라미터로 들어오지 않으면 기본 index 0 에 set
			f = StringUtils.trimToEmpty(f);
			if (f.isEmpty())
				requestPrototype.set(v);
			else
				requestPrototype.set(f, v);

			HopoService service = serviceRegistry.getService(entity);

			return ResponseEntity.ok(dtoRegistry.getResponse(entity, "show").of(service.show(requestPrototype, f, entity)));
		} catch (Exception e) {
			log.error("데이터를 불러오는데 실패했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException("NO_SUCH_DATA");
		}
	}

	@GetMapping("/hopo/show_all")
	@Operation(summary = "조회", description = "전체 조회")
	protected ResponseEntity<List<HopoDto>> showAll(@PathVariable String entity) {
		try {
			// Service class 결정
			HopoService service = serviceRegistry.getService(entity);

			// Request class 결정
			HopoDto responsePrototype = dtoRegistry.getResponse(entity, "show");

			List<HopoDto> entityList = (List<HopoDto>)service.showAll(entity).stream()
				.map(responsePrototype::of)
				.collect(Collectors.toList());

			return ResponseEntity.ok(entityList);
		} catch (Exception e) {
			log.error("데이터를 불러오는데 실패했습니다. \n Message: {}", e.getMessage());
			throw new HttpCodeHandleException("SYSTEM_ERROR");
		}
	}
}
