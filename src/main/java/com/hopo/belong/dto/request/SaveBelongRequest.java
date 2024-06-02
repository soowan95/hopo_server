package com.hopo.belong.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "그룹 생성 요청")
public class SaveBelongRequest {

	@Schema(description = "그룹코드")
	private String code;
	@Schema(description = "주소")
	private String address;
	@Schema(description = "회사 여부")
	private boolean isCompany;
}
