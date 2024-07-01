package com.ssu.goodplassu.ad.dto.response;

import com.ssu.goodplassu.ad.entity.Ad;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class AdListResponse {
	private final Long id;
	private final String image;
	private final String link;

	public static AdListResponse of(
			final Ad ad
	) {
		return new AdListResponse(
				ad.getId(),
				ad.getImage(),
				ad.getLink()
		);
	}
}
