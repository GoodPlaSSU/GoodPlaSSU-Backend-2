package com.ssu.goodplassu.board.dto.response;

import com.ssu.goodplassu.board.entity.Board;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class PostModifyResponse {
	private final Long id;

	public static PostModifyResponse of(final Board board) {
		return new PostModifyResponse(
				board.getId()
		);
	}
}
