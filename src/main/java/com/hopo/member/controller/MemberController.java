package com.hopo.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hopo.member.dto.request.SignUpRequest;
import com.hopo.member.dto.response.MemberResponse;
import com.hopo.member.service.MemberServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@Tag(name = "사용자", description = "사용자 API 문서")
@RequiredArgsConstructor
public class MemberController {

	private final MemberServiceImpl memberService;

	@PostMapping("/sign_up")
	public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) {
		return ResponseEntity.ok(memberService.signUp(request));
	}
}
