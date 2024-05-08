package com.ssu.goodplassu.member.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Member {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String portrait;
	private Long totalPoint = 0L;
	private Long monthPoint = 0L;
	@Enumerated(EnumType.STRING)
	private Role role;

	public void increaseTotalPoint() {
		this.totalPoint++;
	}

	public void increaseMonthPoint() {
		this.monthPoint++;
	}

	public Member update(String name, String portrait) {
		this.name = name;
		this.portrait = portrait;

		return this;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}
