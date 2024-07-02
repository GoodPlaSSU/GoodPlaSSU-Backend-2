package com.ssu.goodplassu.ad.controller;

import com.ssu.goodplassu.ad.dto.response.AdListResponse;
import com.ssu.goodplassu.ad.openapi.AdApi;
import com.ssu.goodplassu.ad.service.AdService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class AdController implements AdApi {
	private final AdService adService;

	@GetMapping("/")
	public ResponseEntity<?> getAds(@RequestParam(value = "page", defaultValue = "0") int page) {
		Page<AdListResponse> adListResponse = adService.getAdList(page);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"광고 리스트를 불러왔습니다.",
						adListResponse
				)
		);
	}
}
