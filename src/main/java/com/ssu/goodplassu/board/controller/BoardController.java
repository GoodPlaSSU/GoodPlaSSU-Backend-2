package com.ssu.goodplassu.board.controller;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.request.PostModifyRequest;
import com.ssu.goodplassu.board.dto.response.BoardDetailResponse;
import com.ssu.goodplassu.board.dto.response.BoardListResponse;
import com.ssu.goodplassu.board.dto.response.PostCreateResponse;
import com.ssu.goodplassu.board.dto.response.PostModifyResponse;
import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.openapi.BoardApi;
import com.ssu.goodplassu.board.service.BoardService;
import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import com.ssu.goodplassu.common.config.auth.util.SecurityUtils;
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

	@GetMapping("/")
	public ResponseEntity<?> getBoards(
			@RequestParam(name = "tag") final int tagAsInt,
			@RequestParam(value = "page", defaultValue = "0") int page
	) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		// 0: 선행게시판, 1: 참여게시판
		boolean tag = false;
		if (tagAsInt == 1) {
			tag = true;
		} else if (tagAsInt == 0) {
			tag = false;
		} else {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 리스트 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		Page<BoardListResponse> boardListResponse = boardService.findBoardList(tag, page, userDto);

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
		SecurityUserDto userDto = SecurityUtils.getUser();

		BoardDetailResponse boardDetailResponse = boardService.findBoardById(postId, userDto);
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
		SecurityUserDto userDto = SecurityUtils.getUser();

		PostCreateResponse postCreateResponse = boardService.createPost(postCreateRequest, multipartFiles, userDto);
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

	@PostMapping(path = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> modifyPost(
			@PathVariable("postId") final Long postId,
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostModifyRequest postModifyRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		PostModifyResponse postModifyResponse = boardService.modifyPost(postId, postModifyRequest, multipartFiles, userDto);
		if (postModifyResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 수정에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물을 수정했습니다.",
						postModifyResponse
				)
		);
	}

	@DeleteMapping(path = "/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable("postId") final Long postId) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		Board result = boardService.deletePost(postId, userDto);
		if (result == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 삭제에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.NO_CONTENT.value(),
						"게시물을 삭제했습니다.",
						List.of()
				)
		);
	}
}
