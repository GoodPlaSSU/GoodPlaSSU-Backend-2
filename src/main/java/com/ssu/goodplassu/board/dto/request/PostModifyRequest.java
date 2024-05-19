package com.ssu.goodplassu.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class PostModifyRequest {
	private String content;
	private boolean tag;
	private List<String> delete_images;
}
