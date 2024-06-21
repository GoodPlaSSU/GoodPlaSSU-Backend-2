package com.ssu.goodplassu.login.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthTokenResponse {
	private final String access_token;

	public static AuthTokenResponse of(final String accessToken) {
		return new AuthTokenResponse(accessToken);
	}
}
