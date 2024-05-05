package com.ssu.goodplassu.board.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Board", description = "게시판 관련 API")
public interface BoardApi {

	@Operation(
			summary = "게시판 게시물 리스트",
			description = "커서 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponse(
			responseCode = "200",
			description = "게시물 리스트 조회 성공"
	)
	@GetMapping("")
	public ResponseEntity<?> getBoards(
			@RequestParam(name = "tag") int tagAsInt,
			@RequestParam(name = "cursor") String cursor,
			@RequestParam(name = "user_key", required = false) Long userId
	);
}
