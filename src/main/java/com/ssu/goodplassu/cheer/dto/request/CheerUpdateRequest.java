package com.ssu.goodplassu.cheer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class CheerUpdateRequest {
	private Long post_id;
	private Long user_id;	// 로그인을 한 멤버(좋아요를 누르는 사람), 로그인하지 않은 상태에서는 null
}
