package com.ssu.goodplassu.cheer.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CheerUpdateResponse {
	private final boolean like_on;

	public static CheerUpdateResponse of(final boolean isOn) {
		return new CheerUpdateResponse(
				isOn
		);
	}
}
