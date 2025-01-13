package com.hopo.belong.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(name = "같은 공간 작업하는 구성원 이름")
public class FamilyNameResponse {

	private List<String> familyNames;
}
