package com.ssu.goodplassu.board.openapi;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.request.PostModifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
			@RequestParam(name = "tag") final int tagAsInt,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(name = "user_key", required = false) final Long userId
	);

	@Operation(
			summary = "게시물 조회",
			description = "게시물 아이디로 게시물 내용 조회"
	)
	@ApiResponse(responseCode = "200", description = "게시물 조회 성공")
	@ApiResponse(responseCode = "400", description = "게시물 조회 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@GetMapping("/{postId}")
	public ResponseEntity<?> getBoardById(
			@PathVariable(value = "postId") final Long postId
	);

	@Operation(
			summary = "게시물 생성",
			description = "게시판에 새 게시물 생성"
	)
	@ApiResponse(responseCode = "201", description = "게시물 생성 성공")
	@ApiResponse(responseCode = "400", description = "게시물 등록 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createPost(
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostCreateRequest postCreateRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	);

	@Operation(
			summary = "게시물 수정",
			description = "기존 게시물 수정"
	)
	@ApiResponse(responseCode = "200", description = "게시물 수정 성공")
	@ApiResponse(responseCode = "400", description = "게시물 수정 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@PostMapping(path = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> modifyPost(
			@PathVariable("postId") final Long postId,
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostModifyRequest postModifyRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	);

	@Operation(
			summary = "게시물 삭제",
			description = "게시물 삭제"
	)
	@ApiResponse(responseCode = "200", description = "게시물 삭제 성공")
	@ApiResponse(responseCode = "400", description = "게시물 삭제 실패")
	@ApiResponse(responseCode = "500", description = "서버 에러")
	@DeleteMapping(path = "/{postId}")
	public ResponseEntity<?> deletePost(
			@PathVariable("postId") final Long postId
	);
}
