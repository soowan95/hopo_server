package com.hopo._global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionEnum {
	DUPLICATE_ID(HttpStatus.BAD_REQUEST, "이미 사용 중인 아이디"),
	NOT_EXIST_CODE(HttpStatus.BAD_REQUEST, "존재 하지 않는 그룹 코드"),
	;

	private final HttpStatus status;
	private final String msg;
}
