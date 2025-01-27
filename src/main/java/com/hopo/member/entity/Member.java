package com.hopo.member.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hopo._global.entity.Hopo;
import com.hopo.space.entity.Space;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends Hopo {

	@Column(name = "login_id", unique = true, nullable = false)
	private String loginId;

	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "email_verified")
	@ColumnDefault("false")
	@Builder.Default
	private Boolean emailVerified = false;

	@Enumerated(EnumType.STRING)
	@ColumnDefault("'BELONG'")
	@Builder.Default
	private Status status = Status.BELONG;

	@Enumerated(EnumType.STRING)
	@ColumnDefault("'MEMBER'")
	@Builder.Default
	private Role role = Role.MEMBER;

	@Column(name = "creatable_space_cnt")
	@ColumnDefault("1")
	@Builder.Default
	private Integer creatableSpaceCnt = 1;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@Column(name = "date_to_frozen")
	private LocalDateTime dateToFrozen;

	@Column(name = "closed_date")
	private LocalDateTime closedDate;

	@OneToMany(mappedBy = "member")
	private List<Space> spaces;

	@PrePersist
	protected void onCreate() {
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
	}

	public Member hashPassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
		return this;
	}

	public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(plainPassword, this.password);
	}
}
