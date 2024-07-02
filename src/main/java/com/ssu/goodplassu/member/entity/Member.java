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
	@Column(unique = true)
	private String email;
	private String portrait;
	@Builder.Default
	private Long totalPoint = 0L;
	@Builder.Default
	private Long monthPoint = 0L;
	@Enumerated(EnumType.STRING)
	private Role role;

	public void increaseTotalPoint() {
		this.totalPoint++;
	}

	public void increaseMonthPoint() {
		this.monthPoint++;
	}

	public void updateInfo(String name, String portrait) {
		this.name = name;
		this.portrait = portrait;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}
