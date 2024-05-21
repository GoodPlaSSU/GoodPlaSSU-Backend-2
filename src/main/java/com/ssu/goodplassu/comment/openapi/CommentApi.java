package com.ssu.goodplassu.comment.openapi;

import com.ssu.goodplassu.comment.dto.request.CommentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Comment", description = "댓글 관련 API")
public interface CommentApi {

	@Operation(
			summary = "댓글 생성",
			description = "게시물에 댓글 생성"
	)
	@ApiResponse(responseCode = "201", description = "댓글 생성 성공")
	@ApiResponse(responseCode = "400", description = "댓글 생성 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@PostMapping("/")
	public ResponseEntity<?> createComment(@RequestBody final CommentCreateRequest commentCreateRequest);

	@Operation(
			summary = "댓글 삭제",
			description = "게시물에 달린 댓글 삭제"
	)
	@ApiResponse(responseCode = "201", description = "댓글 삭제 성공")
	@ApiResponse(responseCode = "400", description = "댓글 삭제 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") final Long commentId);
}
