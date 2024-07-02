package com.ssu.goodplassu.member.dto.response;

import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberInfoResponse {
	private final String name;
	private final String email;
	private final String profile;
	private final Long total_point;
	private final Long month_point;

	public static MemberInfoResponse of(final Member member) {
		return new MemberInfoResponse(
				member.getName(),
				member.getEmail(),
				member.getPortrait(),
				member.getTotalPoint(),
				member.getMonthPoint()
		);
	}
}
