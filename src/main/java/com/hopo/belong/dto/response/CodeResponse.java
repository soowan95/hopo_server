package com.hopo.belong.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "코드 생성 응답")
public class CodeResponse {

	@Schema(name = "코드")
	private String code;
}
