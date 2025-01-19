package com.hopo._global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 동적 응답코드 Exception 목록
 */
@Getter
@AllArgsConstructor
public enum HttpCodeHandleExceptionEnum {
	DUPLICATE_ID(HttpStatus.UNAUTHORIZED, "이미 사용 중인 아이디입니다."), // 401
	DUPLICATE_CODE(HttpStatus.UNAUTHORIZED, "중복된 코드가 존재합니다."), // 401
	NOT_EXIST_BELONG(HttpStatus.NO_CONTENT, "소속이 존재하지 않습니다."), // 204
	NOT_EXIST_CODE(HttpStatus.NO_CONTENT, "존재 하지 않는 소속 코드입니다."), // 204
	NO_SUCH_DATA(HttpStatus.NO_CONTENT, "데이터가 없습니다."), // 204
	NO_SUCH_FIELD(HttpStatus.NO_CONTENT, "존재 하지 않는 컬럼입니다."), // 204
	NOT_EXPECTED_RETURN_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "응답 형이 올바르지 않습니다."), // 500
	NO_SUCH_SERVICE(HttpStatus.BAD_REQUEST, "존재 하지 않는 Service 입니다.") // 404
	;

	private final HttpStatus status;
	private final String msg;
}
