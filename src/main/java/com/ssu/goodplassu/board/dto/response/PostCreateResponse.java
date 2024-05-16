package com.ssu.goodplassu.board.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class PostCreateResponse {
	private final Long id;

	public static PostCreateResponse of(final Long id) {
		return new PostCreateResponse(
				id
		);
	}
}
