package com.hopo.member.dto.request;

import com.hopo.member.entity.Role;
import com.hopo.member.entity.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "회원 가입 요청")
public class SignUpRequest {

	@Schema(name = "아이디")
	private String loginId;

	@Schema(name = "비밀번호")
	private String password;

	@Schema(name = "이름")
	private String name;

	@Schema(name = "이메일")
	private String email;

	@Schema(name = "주소")
	private String address;

	@Schema(name = "상태")
	private Status status;

	@Schema(name = "권한")
	private Role role;

	@Schema(name = "소속 코드")
	private String belongCode;
}
