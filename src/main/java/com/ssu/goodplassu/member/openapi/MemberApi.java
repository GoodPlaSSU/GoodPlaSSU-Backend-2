package com.ssu.goodplassu.member.openapi;

import com.ssu.goodplassu.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberApi {

	@Operation(
			summary = "이달의 선행왕 리스트",
			description = "month_point가 가장 높은 4명"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "이달의 선행왕 4명 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 200," +
											"\"success\": \"이달의 선행왕 목록을 불러왔습니다.\"," +
											"\"data\": [" +
													"{\n" +
													"      \"writer_name\": \"Alice Johnson\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"month_point\": 80\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"Jane Smith\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"month_point\": 75\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"Bob Brown\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"month_point\": 60\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"John Doe\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"month_point\": 50\n" +
													"    }\n" +
											"]" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/month")
	public ResponseEntity<?> getHighestMonthPoint();

	@Operation(
			summary = "전체 선행왕 리스트",
			description = "total_point가 가장 높은 4명"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "전체 선행왕 4명 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 200," +
											"\"success\": \"전체 선행왕 목록을 불러왔습니다.\"," +
											"\"data\": [" +
													"{\n" +
													"      \"writer_name\": \"Alice Johnson\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"total_point\": 200\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"Bob Brown\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"total_point\": 180\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"Jane Smith\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"total_point\": 150\n" +
													"    },\n" +
													"    {\n" +
													"      \"writer_name\": \"John Doe\",\n" +
													"      \"writer_profile\": \"portrait_url_here\",\n" +
													"      \"total_point\": 100\n" +
													"    }" +
											"]" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/total")
	public ResponseEntity<?> getHighestTotalPoint();
}
