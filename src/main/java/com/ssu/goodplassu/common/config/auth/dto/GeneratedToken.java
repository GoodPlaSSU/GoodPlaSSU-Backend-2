package com.ssu.goodplassu.common.config.auth.dto;

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
