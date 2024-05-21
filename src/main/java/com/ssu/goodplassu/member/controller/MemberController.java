package com.ssu.goodplassu.member.controller;

import com.ssu.goodplassu.common.dto.ResponseDto;
import com.ssu.goodplassu.member.dto.response.HighestMonthPointResponse;
import com.ssu.goodplassu.member.openapi.MemberApi;
import com.ssu.goodplassu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberApi {
	private final MemberService memberService;

	@GetMapping("/month")
	public ResponseEntity<?> getHighestMonthPoint() {
		List<HighestMonthPointResponse> highestMonthPointResponseList = memberService.getHighestMonthPointMembers();

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"이달의 선행왕 목록을 불러왔습니다.",
						highestMonthPointResponseList
				)
		);
	}
}
