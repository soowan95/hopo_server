package com.hopo.belong.entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.hopo._global.entity.Hopo;
import com.hopo.space.entity.Space;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Belong extends Hopo {

	@Id
	@Column(name="code", unique = true, nullable = false)
	private String code;

	@Column(name="address")
	private String address;

	@Column(name="isCompany")
	@ColumnDefault("false")
	@Builder.Default
	private Boolean isCompany = false;

	@OneToMany(mappedBy = "belong")
	private List<Space> spaces;
}
