package com.ssu.goodplassu.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class PostCreateRequest {
	private Long writer_id;
	private String content;
	private boolean tag;
}
