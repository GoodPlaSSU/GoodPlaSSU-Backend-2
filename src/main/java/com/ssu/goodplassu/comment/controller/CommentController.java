package com.ssu.goodplassu.comment.controller;

import com.ssu.goodplassu.comment.dto.request.CommentCreateRequest;
import com.ssu.goodplassu.comment.dto.response.CommentCreateResponse;
import com.ssu.goodplassu.comment.service.CommentService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
	private final CommentService commentService;

	@PostMapping("/")
	public ResponseEntity<?> createComment(@RequestBody final CommentCreateRequest commentCreateRequest) {
		CommentCreateResponse commentCreateResponse = commentService.createComment(commentCreateRequest);
		if (commentCreateResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"댓글 생성에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"댓글을 생성했습니다.",
						commentCreateResponse
				)
		);
	}
}
