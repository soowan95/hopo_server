package com.hopo.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
	BELONG("BELONG", ""),
	SINGLE("SINGLE", ""),
	STANDBY("STANDBY", "관리자 승인 대기중입니다."),
	FROZEN("FROZEN", "이사한 주소로 변경 후 재사용 가능합니다."),
	CLOSED("CLOSED", "삭제 된 계정입니다."),
	;

	private final String status;
	private final String info;
}
