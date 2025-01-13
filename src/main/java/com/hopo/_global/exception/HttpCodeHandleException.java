package com.hopo._global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 동적 응답코드를 갖는 Exception Class
 */
@Getter
public class HttpCodeHandleException extends RuntimeException {

	private final HttpStatus status;
	private final String msg;

	/**
	 * HttpCodeHandleExceptionEnum 으로 예외처리
	 * @param enumName {@link String String}
	 */
	public HttpCodeHandleException(String enumName) {
		HttpCodeHandleExceptionEnum httpCodeHandleExceptionEnum = HttpCodeHandleExceptionEnum.valueOf(enumName);
		this.status = httpCodeHandleExceptionEnum.getStatus();
		this.msg = httpCodeHandleExceptionEnum.getMsg();
	}

	/**
	 * 동적으로 예외처리
	 * @param code {@link Integer Integer} http status code
	 * @param msg {@link String String}
	 */
	public HttpCodeHandleException(Integer code, String msg) {
		this.status = HttpStatus.valueOf(code);
		this.msg = msg;
	}
}
