package com.ssu.goodplassu.image.dto.response;

import com.ssu.goodplassu.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImagesResponse {
	private List<String> imageUrls;

	public static ImagesResponse of(final List<Image> images) {
		return new ImagesResponse(
				images.stream()
						.map(Image::getUrl)
						.toList()
		);
	}
}
