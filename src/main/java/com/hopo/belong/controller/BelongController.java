package com.hopo.belong.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopo._config.registry.DtoRegistry;
import com.hopo._config.registry.ServiceRegistry;
import com.hopo._global.controller.HopoController;
import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.dto.response.CodeResponse;
import com.hopo.belong.dto.response.FamilyNameResponse;
import com.hopo.belong.dto.response.SaveBelongResponse;
import com.hopo.belong.service.BelongServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/belong")
@Tag(name = "소속", description = "소속 API 문서")
public class BelongController extends HopoController {

	private final BelongServiceImpl belongService;

	public BelongController(ServiceRegistry serviceRegistry,
		DtoRegistry dtoRegistry, BelongServiceImpl belongService) {
		super(serviceRegistry, dtoRegistry);
		this.belongService = belongService;
	}

	@PostMapping("/save")
	@Operation(summary = "소속 정보 추가", description = "회원가입 시 처음 등록되는 주소에 대한 정보 추가")
	public ResponseEntity<SaveBelongResponse> save(@RequestBody SaveBelongRequest saveBelongRequest) {
		return ResponseEntity.ok(belongService.save(saveBelongRequest));
	}

	@GetMapping("/make_code")
	@Operation(summary = "소속 코드 발급", description = "신규 소속 코드 발급")
	public ResponseEntity<CodeResponse> makeCode(@RequestParam String address) {
		return ResponseEntity.ok(belongService.makeCode(address));
	}

	@GetMapping("/is_family")
	@Operation(summary = "가족 구성원 확인", description = "가족 구성원 이름 마스킹해서 응답 - 요청 주소에 가족 구성원 없으면 throw CustomException")
	public ResponseEntity<FamilyNameResponse> retrieveFamilyName(@RequestParam String address) {
		return ResponseEntity.ok(belongService.findFamilyName(address));
	}
}
