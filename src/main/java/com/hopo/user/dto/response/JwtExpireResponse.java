package com.hopo.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtExpireResponse {

	private String msg;
	private Integer code;
}
