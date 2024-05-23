package com.ssu.goodplassu.cheer.controller;

import com.ssu.goodplassu.cheer.dto.request.CheerOnRequest;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.openapi.CheerApi;
import com.ssu.goodplassu.cheer.service.CheerService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cheers")
public class CheerController implements CheerApi {
	private final CheerService cheerService;

	@PostMapping("/like")
	public ResponseEntity<?> setCheerOn(@RequestBody final CheerOnRequest cheerOnRequest) {
		Cheer cheer = cheerService.setCheerOn(cheerOnRequest);
		if (cheer == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 좋아요에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물 좋아요에 성공했습니다.",
						List.of()
				)
		);
	}
}
