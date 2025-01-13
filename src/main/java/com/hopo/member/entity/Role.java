package com.hopo.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	SYSTEM_ADMIN,
	COMPANY_ADMIN,
	ADMIN,
	MEMBER;
}
