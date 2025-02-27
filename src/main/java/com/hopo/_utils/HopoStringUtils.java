package com.hopo._utils;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HopoStringUtils {
	/**
	 * get 메서드를 만들기 위해 첫글자 대문자화
	 * @param value {@link String String} 카멜케이스 *
	 * @return String
	 */
	public static String capitalize(String value) {
		if (value == null || value.isBlank())	return "";
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	/**
	 * filed name 을 가져오기 위해 첫글자 소문자화
	 * @param value {@link String String} 카멜케이스*
	 * @return String
	 */
	public static String uncapitalize(String value) {
		if (value == null || value.isBlank())	return "";
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}

	/**
	 * 짝수 index 문자 * 로 치환
	 * @param value {@link String String}
	 * @return String
	 */
	public static String masking(String value) {
		if (value == null || value.isBlank())	return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (i % 2 == 0)
				sb.append(value.charAt(i));
			else
				sb.append("*");
		}

		return sb.toString();
	}

	/**
	 * 각 case 별로 split 후 소문자 치환하여 반환한다
	 * <p>
	 *   camelCase - camel, case <br/>
	 *   PascalCase - pascal, case <br/>
	 *   snake_case - snake, case <br/>
	 *   kebab-case - kebab, case
	 * </p>
	 * @param value {@link String String}
	 * @return String[]
	 */
	public static String[] split(String value) {
		if (value == null || value.isBlank())	return new String[]{""};
		if (value.matches("^[a-z]+(?:[A-Z][a-z]*)*$")) {
			return Arrays.stream(value.split("(?=[A-Z])")).map(HopoStringUtils::uncapitalize).toArray(String[]::new);
		} else if (value.matches("^[A-Z][a-z]*(?:[A-Z][a-z]*)*$")) {
			return Arrays.stream(value.split("(?=[A-Z])")).filter(s -> !s.isEmpty()).map(HopoStringUtils::uncapitalize).toArray(String[]::new);
		} else if (value.matches("^[a-z]+(?:_[a-z]+)*$")) {
			return value.split("_");
		} else if (value.matches("^[a-z]+(?:-[a-z]+)*$")) {
			return value.split("-");
		} else {
			return new String[]{value};
		}
	}
}
