package com.hopo.member.dto.response;

import com.hopo._global.dto.HopoDto;
import com.hopo.member.entity.Member;
import com.hopo.member.entity.Role;
import com.hopo.member.entity.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(name = "사용자 정보")
public class MemberResponse extends HopoDto<MemberResponse, Member> {

	@Schema(name = "아이디")
	private String id;

	@Schema(name = "비밀번호")
	private String password;

	@Schema(name = "이름")
	private String name;

	@Schema(name = "이메일")
	private String email;

	@Schema(name = "상태")
	private Status status;

	@Schema(name = "권한")
	private Role role;
}
