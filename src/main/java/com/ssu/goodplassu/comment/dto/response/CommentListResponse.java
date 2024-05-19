package com.ssu.goodplassu.comment.dto.response;

import com.ssu.goodplassu.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CommentListResponse {
	private final Long id;
	private final String writer_name;
	private final String writer_profile;
	private final String content;
	private final LocalDateTime created_at;

	public static CommentListResponse of(final Comment comment) {
		return new CommentListResponse(
			comment.getId(),
			comment.getMember().getName(),
			comment.getMember().getPortrait(),
			comment.getContent(),
			comment.getCreatedAt()
		);
	}
}
