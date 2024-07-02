package com.ssu.goodplassu.login.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUserDto {
	private Long id;
	private String email;
	private String name;
	private String picture;
	private String role;
}
