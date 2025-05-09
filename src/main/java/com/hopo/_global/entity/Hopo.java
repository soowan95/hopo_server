package com.hopo._global.entity;

import java.lang.reflect.Method;

import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._utils.HopoStringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 최상위 Entity Class
 */
@Slf4j
public class Hopo {
	/**
	 * 숨김 처리한 data 를 가져온다
	 * @param property {@link String String} column 명
	 * @return String
	 */
	public String getMaskData(String property) {
		try {
			String methodName = "get" + HopoStringUtils.capitalize(property);

			Method method = this.getClass().getMethod(methodName);

			Object result = method.invoke(this);

			if (!(result instanceof String)) {
				log.warn("The type id " + result.getClass().getName());
				throw new HttpCodeHandleException("NOT_EXPECTED_RETURN_TYPE");
			}

			return HopoStringUtils.masking(result.toString());
		} catch (Exception e) {
			log.error("Caught Exception: " + e.getMessage());
			throw new HttpCodeHandleException(500, "서버에서 오류가 발생했습니다.");
		}
	}
}
