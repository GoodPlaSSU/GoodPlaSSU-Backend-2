package com.ssu.goodplassu.comment.dto.response;

import com.ssu.goodplassu.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CommentCreateResponse {
	private final Long id;

	public static CommentCreateResponse of(final Comment comment) {
		return new CommentCreateResponse(
				comment.getId()
		);
	}
}
