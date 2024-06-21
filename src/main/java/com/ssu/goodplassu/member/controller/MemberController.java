package com.ssu.goodplassu.member.controller;

import com.ssu.goodplassu.common.dto.ResponseDto;
import com.ssu.goodplassu.login.dto.SecurityUserDto;
import com.ssu.goodplassu.login.util.SecurityUtils;
import com.ssu.goodplassu.member.dto.response.HighestMonthPointResponse;
import com.ssu.goodplassu.member.dto.response.HighestTotalPointResponse;
import com.ssu.goodplassu.member.dto.response.MemberInfoResponse;
import com.ssu.goodplassu.member.dto.response.MemberPostListResponse;
import com.ssu.goodplassu.member.openapi.MemberApi;
import com.ssu.goodplassu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/total")
	public ResponseEntity<?> getHighestTotalPoint() {
		List<HighestTotalPointResponse> highestTotalPointResponseList = memberService.getHighestTotalPointMembers();

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"전체 선행왕 목록을 불러왔습니다.",
						highestTotalPointResponseList
				)
		);
	}

	@GetMapping("/info")
	public ResponseEntity<?> getMemberInfo() {
		SecurityUserDto userDto = SecurityUtils.getUser();

		MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(userDto);
		if (memberInfoResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"사용자 정보 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"사용자 정보 조회에 성공했습니다.",
						memberInfoResponse
				)
		);
	}

	@GetMapping("/posts")
	public ResponseEntity<?> getMemberPosts(@RequestParam(value = "page", defaultValue = "0") int page) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		Page<MemberPostListResponse> memberPostListResponseList = memberService.getMemberPosts(page, userDto);
		if (memberPostListResponseList == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"사용자가 작성한 게시물 리스트 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"사용자가 작성한 게시물 리스트를 불러왔습니다.",
						memberPostListResponseList
				)
		);
	}

	@GetMapping("/comments/posts")
	public ResponseEntity<?> getMemberCommentPosts(@RequestParam(value = "page", defaultValue = "0") int page) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		Page<MemberPostListResponse> memberPostListResponseList = memberService.getMemberCommentPosts(page, userDto);
		if (memberPostListResponseList == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"사용자가 댓글을 작성한 게시물 리스트 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"사용자가 댓글을 작성한 게시물 리스트를 불러왔습니다.",
						memberPostListResponseList
				)
		);
	}

	@GetMapping("/likes/posts")
	public ResponseEntity<?> getMemberLikePosts(@RequestParam(value = "page", defaultValue = "0") int page) {
		SecurityUserDto userDto = SecurityUtils.getUser();

		Page<MemberPostListResponse> memberPostListResponseList = memberService.getMemberLikePosts(page, userDto);
		if (memberPostListResponseList == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.BAD_REQUEST.value(),
							"사용자가 좋아요를 누를 게시물 리스트 조회에 실패했습니다.",
							List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"사용자가 좋아요를 누른 게시물 리스트를 불러왔습니다.",
						memberPostListResponseList
				)
		);
	}
}
