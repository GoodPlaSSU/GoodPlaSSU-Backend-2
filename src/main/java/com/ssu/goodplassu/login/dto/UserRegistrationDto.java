package com.ssu.goodplassu.login.dto;

import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.entity.Role;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class UserRegistrationDto {
	private String email;
	private String name;
	private String picture;
	private Role role;

	public Member toEntity() {
		return Member.builder()
				.email(email)
				.name(name)
				.portrait(picture)
				.build();
	}
}
