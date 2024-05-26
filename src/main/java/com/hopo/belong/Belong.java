package com.hopo.belong;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Belong {

	@Id
	@Column(name="code", unique = true, nullable = false)
	String code;

	@Column(name="address")
	String address;

	@Column(name="isCompany")
	boolean isCompany;
}
