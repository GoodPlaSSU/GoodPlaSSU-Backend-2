package com.ssu.goodplassu.login.dto;

import com.ssu.goodplassu.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleInfoDto {
	private String email;
	private String name;
	private String picture;

	public UserRegistrationDto toUserRegistrationDto() {
		return UserRegistrationDto.builder()
				.email(email)
				.name(name)
				.picture(picture)
				.role(Role.USER)
				.build();
	}
}
