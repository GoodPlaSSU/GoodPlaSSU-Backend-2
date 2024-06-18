package com.ssu.goodplassu.cheer.openapi;

import com.ssu.goodplassu.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Cheer", description = "게시글에 좋아요 추가 및 삭제 관련 API")
public interface CheerApi {

	@Operation(
			summary = "게시글에 좋아요 추가 및 취소",
			description = "좋아요 추가 후 해당 게시글 작성자의 포인트 증가"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "게시물 좋아요 추가 및 취소 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 200," +
											"\"succes\": \"게시물 좋아요 추가 및 취소에 성공했습니다.\"," +
											"\"data\": {" +
													"\"like_on\": true" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "게시물 좋아요 추가 및 취소 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 좋아요 추가 및 취소에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping("/{postId}")
	public ResponseEntity<?> setCheerOnOff(@PathVariable("postId") final Long postId);
}
