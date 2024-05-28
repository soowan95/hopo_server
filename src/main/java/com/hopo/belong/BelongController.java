package com.hopo.belong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hopo.belong.dto.request.SaveBelongRequest;
import com.hopo.belong.service.BelongServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/belong")
@Tag(name = "소속", description = "소속 API 문서")
@RequiredArgsConstructor
public class BelongController {

	private final BelongServiceImpl belongService;

	@PostMapping("/save")
	@Operation(summary = "소속 정보 추가", description = "회원가입 시 처음 등록되는 주소에 대한 정보 추가")
	public ResponseEntity<Void> save(@RequestBody SaveBelongRequest saveBelongRequest) {
		belongService.save(saveBelongRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/make_code")
	@Operation(summary = "소속 코드 발급", description = "주소 중복 확인: true -> 소속 구성원 확인, false -> 새로운 소속 코드 발급")
	public ResponseEntity<String> makeCode(@RequestParam String address) {
		return ResponseEntity.ok(belongService.makeCode(address));
	}
}
