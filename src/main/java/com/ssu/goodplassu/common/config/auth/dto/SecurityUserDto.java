package com.ssu.goodplassu.common.config.auth.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUserDto {
	private Long id;
	private String email;
	private String nickname;
	private String picture;
	private String role;
}
