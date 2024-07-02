package com.ssu.goodplassu.member.dto.response;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.cheer.entity.Cheer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberPostListResponse {
	private final Long id;
	private final String writer_profile;
	private final String writer_name;
	private final String content;
	private final String thumbnail;
	private final boolean tag;
	private final boolean like_on;
	private final long like_count;
	private final LocalDateTime updated_at;
	private final long comment_count;

	public static MemberPostListResponse of(
			final Board board,
			final Cheer cheer,
			final long cheerCnt,
			final long commentCnt
	) {
		String imageUrl = "";
		if (!board.getImages().isEmpty()) {
			imageUrl = board.getImages().get(0).getUrl(); // 첫 번째 이미지의 URL을 가져옵니다.
		}

		return new MemberPostListResponse(
				board.getId(),
				board.getMember().getPortrait(),
				board.getMember().getName(),
				board.getContent(),
				imageUrl,
				board.isTag(),
				cheer == null ? false : cheer.isOn(),	// 로그인하지 않은 경우 null -> 좋아요(cheer) 누르지 않은 상태
				cheerCnt,
				board.getUpdatedAt(),
				commentCnt
		);
	}
}
