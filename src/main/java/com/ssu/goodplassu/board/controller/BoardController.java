package com.ssu.goodplassu.board.controller;

import com.ssu.goodplassu.board.dto.BoardListResponse;
import com.ssu.goodplassu.board.openapi.BoardApi;
import com.ssu.goodplassu.board.service.BoardService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController implements BoardApi {
	private final BoardService boardService;

	@GetMapping("")
	public ResponseEntity<?> getBoards(
			@RequestParam(name = "tag") int tagAsInt,
			@RequestParam(name = "cursor") String cursor,
			@RequestParam(name = "user_key", required = false) Long userId
	) {
		// 0: 선행게시판, 1: 참여게시판
		boolean tag = tagAsInt == 1 ? true : false;
		List<BoardListResponse> boardListResponse = boardService.findBoardList(tag, cursor, userId);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물 리스트를 불러왔습니다.", null, null,
						boardListResponse
				)
		);
	}
}
