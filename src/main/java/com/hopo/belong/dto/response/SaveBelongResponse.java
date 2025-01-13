package com.hopo.belong.dto.response;

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
@Schema(name = "생성된 소속 코드 응답")
public class SaveBelongResponse extends HopoDto<SaveBelongResponse, Belong> {

	@Schema(name = "생성된 소속 코드")
	private String code;
}