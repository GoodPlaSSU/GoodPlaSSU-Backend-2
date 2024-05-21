package com.ssu.goodplassu.member.dto.response;

import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HighestTotalPointResponse {
	private final String writer_name;
	private final String writer_profile;
	private final Long total_point;

	public static HighestTotalPointResponse of(final Member member) {
		return new HighestTotalPointResponse(
				member.getName(),
				member.getPortrait(),
				member.getTotalPoint()
		);
	}
}
