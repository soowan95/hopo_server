package com.hopo._global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final HttpStatus status;
	private final String msg;

	public CustomException(CustomExceptionEnum customEnum) {
		this.status = customEnum.getStatus();
		this.msg = customEnum.getMsg();
	}
}
