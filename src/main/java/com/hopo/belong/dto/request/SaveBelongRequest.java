package com.hopo.belong.dto.request;

import com.hopo._global.dto.HopoDto;
import com.hopo.belong.entity.Belong;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 생성 요청")
public class SaveBelongRequest extends HopoDto<SaveBelongRequest, Belong> {

	@Schema(description = "그룹코드")
	private String code;
	@Schema(description = "주소")
	private String address;
}
