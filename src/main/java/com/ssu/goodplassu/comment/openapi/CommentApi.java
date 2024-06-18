package com.ssu.goodplassu.comment.openapi;

import com.ssu.goodplassu.comment.dto.request.CommentCreateRequest;
import com.ssu.goodplassu.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "댓글 생성 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 201," +
											"\"success\": \"댓글을 생성했습니다.\"," +
											"\"data\": {" +
													"\"id\": 1" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "댓글 생성 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"댓글 생성에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping("/")
	public ResponseEntity<?> createComment(@RequestBody final CommentCreateRequest commentCreateRequest);

	@Operation(
			summary = "댓글 삭제",
			description = "게시물에 달린 댓글 삭제"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "댓글 삭제 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 204," +
											"\"success\": \"댓글을 삭제했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "댓글 삭제 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 삭제에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") final Long commentId);
}
