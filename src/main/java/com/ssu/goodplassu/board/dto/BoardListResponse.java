package com.ssu.goodplassu.board.dto;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardListResponse {
	// post id, writer portrait, writer name, updated at, content, image1, is_on, cheer_count
	private final Long id;
	private final String writer_portrait;
	private final String writer_name;
	private final String content;
	private final String image1;
	private final boolean is_on;
	private final Long cheer_count;
	private final LocalDateTime updated_at;
	private final String cursor;

	public static BoardListResponse of(final Board board, final Member member, final Cheer cheer) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formattedDate = formatter.format(board.getCreatedAt());
		String paddedId = String.format("%010d", board.getId());
		String cursor = formattedDate + paddedId;

		return new BoardListResponse(
				board.getId(),
				member.getPortrait(),
				member.getName(),
				board.getContent(),
				board.getImage1(),
				cheer == null ? false : cheer.isOn(),	// 로그인하지 않은 경우 null -> 좋아요(cheer) 누르지 않은 상태
				board.getCheerCount(),
				board.getUpdatedAt(),
				cursor
		);
	}
}
