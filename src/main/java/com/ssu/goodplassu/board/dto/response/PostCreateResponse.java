package com.ssu.goodplassu.board.dto.response;

import com.ssu.goodplassu.board.entity.Board;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class PostCreateResponse {
	private final Long id;

	public static PostCreateResponse of(final Board board) {
		return new PostCreateResponse(
				board.getId()
		);
	}
}
