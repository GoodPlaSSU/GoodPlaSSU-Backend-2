package com.ssu.goodplassu.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssu.goodplassu.member.entity.Role;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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
