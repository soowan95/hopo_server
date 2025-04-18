package com.hopo._utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HopoStringUtils {
	/**
	 * get 메서드를 만들기 위해 첫글자 대문자화
	 * @param property {@link String String} 카멜케이스 *
	 * @return String
	 */
	public static String capitalize(String property) {
		if (property.isEmpty() || property.isBlank())
			return null;
		return property.substring(0, 1).toUpperCase() + property.substring(1).toLowerCase();
	}

	/**
	 * 짝수 index 문자 * 로 치환
	 * @param value {@link String String}
	 * @return String
	 */
	public static String masking(String value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (i % 2 == 0)
				sb.append(value.charAt(i));
			else
				sb.append("*");
		}

		return sb.toString();
	}
}
