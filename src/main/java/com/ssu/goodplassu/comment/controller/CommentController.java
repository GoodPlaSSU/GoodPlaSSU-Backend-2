package com.ssu.goodplassu.comment.controller;

import com.ssu.goodplassu.comment.dto.request.CommentCreateRequest;
import com.ssu.goodplassu.comment.dto.response.CommentCreateResponse;
import com.ssu.goodplassu.comment.entity.Comment;
import com.ssu.goodplassu.comment.openapi.CommentApi;
import com.ssu.goodplassu.comment.service.CommentService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import com.ssu.goodplassu.login.dto.SecurityUserDto;
import com.ssu.goodplassu.login.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentApi {
	private final CommentService commentService;

	@PostMapping("/")
	public ResponseEntity<?> createComment(@RequestBody final CommentCreateRequest commentCreateRequest) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		CommentCreateResponse commentCreateResponse = commentService.createComment(commentCreateRequest, userDto);
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

	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") final Long commentId) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		Comment comment = commentService.deleteComment(commentId, userDto);
		if (comment == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"댓글 삭제에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.NO_CONTENT.value(),
						"댓글을 삭제했습니다.",
						List.of()
				)
		);
	}
}
