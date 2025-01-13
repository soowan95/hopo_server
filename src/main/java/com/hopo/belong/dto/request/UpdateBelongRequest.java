package com.hopo.belong.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "주소 갱신 요청")
public class UpdateBelongRequest {

	@Schema(description = "기존 그룹 코드")
	String code;

	@Schema(description = "갱신할 주소")
	String address;
}
