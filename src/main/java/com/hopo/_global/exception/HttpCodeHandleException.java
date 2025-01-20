package com.hopo._global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 동적 응답코드를 갖는 Exception Class
 */
@Getter
@Slf4j
public class HttpCodeHandleException extends RuntimeException {

	private final HttpStatus status;
	private final String msg;

	/**
	 * HttpCodeHandleExceptionEnum 으로 예외처리
	 * @param enumName {@link String String}
	 */
	public HttpCodeHandleException(String enumName) {
		try {
			HttpCodeHandleExceptionEnum httpCodeHandleExceptionEnum = HttpCodeHandleExceptionEnum.valueOf(enumName);
			this.status = httpCodeHandleExceptionEnum.getStatus();
			this.msg = httpCodeHandleExceptionEnum.getMsg();
		} catch (IllegalArgumentException | NullPointerException e) {
			log.error("HttpCodeHandleExceptionEnum is null. HttpCodeHandleExceptionEnum is {}", enumName);
			throw new HttpCodeHandleException(500, "서버에서 오류가 발생했습니다.");
		}
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
