package com.ssu.goodplassu.member.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberApi {

	@Operation(
			summary = "이달의 선행왕 리스트",
			description = "이달의 month_point가 가장 높은 4명"
	)
	@ApiResponse(responseCode = "200", description = "이달의 선행왕 4명 조회 성공")
	@GetMapping("/month")
	public ResponseEntity<?> getHighestMonthPoint();
}
