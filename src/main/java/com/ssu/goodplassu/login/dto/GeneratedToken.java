package com.ssu.goodplassu.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class GeneratedToken {
	private String accessToken;
	private String refreshToken;
}
