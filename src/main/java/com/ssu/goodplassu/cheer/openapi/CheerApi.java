package com.ssu.goodplassu.cheer.openapi;

import com.ssu.goodplassu.cheer.dto.request.CheerUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cheer", description = "게시글에 좋아요 추가 및 삭제 관련 API")
public interface CheerApi {

	@Operation(
			summary = "게시글에 좋아요 추가",
			description = "좋아요 추가 후 해당 게시글 작성자의 포인트 증가\n좋아요 누르는 게시물 id와 로그인 한 유저 id 필요(로그인하지 않은 경우 null)"
	)
	@ApiResponse(responseCode = "200", description = "게시글 좋아요 성공")
	@ApiResponse(responseCode = "400", description = "게시글 좋아요 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@PostMapping("/like")
	public ResponseEntity<?> setCheerOn(@RequestBody final CheerUpdateRequest cheerUpdateRequest);

	@Operation(
			summary = "게시글에 좋아요 취소",
			description = "좋아요 취소\n좋아요 누르는 게시물 id와 로그인 한 유저 id 필요(로그인하지 않은 경우 null)"
	)
	@ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 성공")
	@ApiResponse(responseCode = "400", description = "게시글 좋아요 취소 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@PostMapping("/unlike")
	public ResponseEntity<?> setCheerOff(@RequestBody final CheerUpdateRequest cheerUpdateRequest);
}
