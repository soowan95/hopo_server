package com.hopo._config.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "토큰 만료 응답")
public class JwtExpireResponse {

	@Schema(name = "메세지")
	private String msg;

	@Schema(name = "코드 번호")
	private Integer code;
}