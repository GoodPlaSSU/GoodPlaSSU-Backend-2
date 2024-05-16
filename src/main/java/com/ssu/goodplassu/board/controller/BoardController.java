package com.ssu.goodplassu.board.controller;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.response.BoardDetailResponse;
import com.ssu.goodplassu.board.dto.response.BoardListResponse;
import com.ssu.goodplassu.board.dto.response.PostCreateResponse;
import com.ssu.goodplassu.board.openapi.BoardApi;
import com.ssu.goodplassu.board.service.BoardService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController implements BoardApi {
	private final BoardService boardService;

	@GetMapping("")
	public ResponseEntity<?> getBoards(
			@RequestParam(name = "tag") final int tagAsInt,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(name = "user_key", required = false) final Long userId
	) {
		// 0: 선행게시판, 1: 참여게시판
		boolean tag = tagAsInt == 1 ? true : false;
		Page<BoardListResponse> boardListResponse = boardService.findBoardList(tag, userId, page);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물 리스트를 불러왔습니다.",
						boardListResponse
				)
		);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<?> getBoardById(@PathVariable(value = "postId") final Long postId) {
		BoardDetailResponse boardDetailResponse = boardService.findBoardById(postId);
		if (boardDetailResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.NOT_FOUND.value(),
							"게시물 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.NOT_FOUND
			);
//			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물을 조회했습니다.",
						boardDetailResponse
				)
		);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createPost(
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostCreateRequest postCreateRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	) {
		PostCreateResponse postCreateResponse = boardService.createPost(postCreateRequest, multipartFiles);
		if (postCreateResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 생성에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
//			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.CREATED.value(),
						"게시물을 생성했습니다.",
						postCreateResponse
				)
		);
	}
}
