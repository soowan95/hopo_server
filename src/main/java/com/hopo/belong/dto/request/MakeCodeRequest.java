package com.hopo.belong.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "코드 발급 요청")
public class MakeCodeRequest {

	@Schema(name = "주소")
	private String address;
}
