package com.hopo._global.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 예외 처리 Class
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpCodeHandleException.class)
	public ResponseEntity<Map<String, String>> handleHttpCodeHandleException(HttpCodeHandleException httpCodeHandleException) {
		log.error("[HttpCodeHandleException] Status: {}, Message: {}", httpCodeHandleException.getStatus(), httpCodeHandleException.getMsg());

		return ResponseEntity.status(httpCodeHandleException.getStatus()).body(Map.of("msg", httpCodeHandleException.getMsg()));
	}
}
