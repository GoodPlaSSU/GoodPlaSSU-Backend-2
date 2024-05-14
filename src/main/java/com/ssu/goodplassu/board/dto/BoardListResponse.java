package com.ssu.goodplassu.board.dto;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardListResponse {
	private final Long id;
	private final String writer_profile;
	private final String writer_name;
	private final String content;
	private final String thumbnail;
	private final boolean like_on;
	private final Long like_count;
	private final LocalDateTime updated_date;

	public static BoardListResponse of(final Board board, final Member member, final Cheer cheer) {
		return new BoardListResponse(
				board.getId(),
				member.getPortrait(),
				member.getName(),
				board.getContent(),
				board.getImage1(),
				cheer == null ? false : cheer.isOn(),	// 로그인하지 않은 경우 null -> 좋아요(cheer) 누르지 않은 상태
				board.getCheerCount(),
				board.getUpdatedAt()
		);
	}
}
