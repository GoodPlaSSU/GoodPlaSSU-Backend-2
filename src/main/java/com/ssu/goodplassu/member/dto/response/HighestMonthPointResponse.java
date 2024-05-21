package com.ssu.goodplassu.member.dto.response;

import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HighestMonthPointResponse {
	private final String writer_name;
	private final String writer_profile;
	private final Long month_point;

	public static HighestMonthPointResponse of(final Member member) {
		return new HighestMonthPointResponse(
				member.getName(),
				member.getPortrait(),
				member.getMonthPoint()
		);
	}
}
