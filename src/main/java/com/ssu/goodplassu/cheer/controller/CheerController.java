package com.ssu.goodplassu.cheer.controller;

import com.ssu.goodplassu.cheer.dto.response.CheerUpdateResponse;
import com.ssu.goodplassu.cheer.openapi.CheerApi;
import com.ssu.goodplassu.cheer.service.CheerService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import com.ssu.goodplassu.login.dto.SecurityUserDto;
import com.ssu.goodplassu.login.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cheers")
public class CheerController implements CheerApi {
	private final CheerService cheerService;

	@PostMapping("/{postId}")
	public ResponseEntity<?> setCheerOnOff(@PathVariable("postId") final Long postId) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		CheerUpdateResponse cheerUpdateResponse = cheerService.setCheerOnOff(postId, userDto);
		if (cheerUpdateResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"게시물 좋아요 추가 및 취소에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"게시물 좋아요 추가 및 취소에 성공했습니다.",
						cheerUpdateResponse
				)
		);
	}
}
