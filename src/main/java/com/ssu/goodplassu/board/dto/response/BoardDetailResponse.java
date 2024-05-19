package com.ssu.goodplassu.board.dto.response;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.comment.dto.response.CommentListResponse;
import com.ssu.goodplassu.image.dto.response.ImagesResponse;
import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class BoardDetailResponse {
	// post id, writer portrait, writer name, updated at, writer id, post image 1~4, post cheer_count
	private final Long id;
	private final Long writer_id;
	private final String writer_name;
	private final String writer_profile;
	private final ImagesResponse images;
	private final LocalDateTime updated_at;
	private final Long like_count;
	private final List<CommentListResponse> comments;

	public static BoardDetailResponse of(final Board board, final Member member, final List<CommentListResponse> commentListResponseList) {
		return new BoardDetailResponse(
			board.getId(),
			member.getId(),
			member.getName(),
			member.getPortrait(),
			ImagesResponse.of(board.getImages()),
			board.getUpdatedAt(),
			board.getCheerCount(),
			commentListResponseList
		);
	}
}
