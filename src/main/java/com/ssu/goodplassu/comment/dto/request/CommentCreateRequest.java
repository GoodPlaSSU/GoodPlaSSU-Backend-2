package com.ssu.goodplassu.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class CommentCreateRequest {
	private Long board_id;
	private Long writer_id;
	private String content;
}
