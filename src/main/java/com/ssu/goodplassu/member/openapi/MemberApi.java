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
import org.springframework.web.bind.annotation.RequestParam;

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

	@Operation(
			summary = "사용자 정보 조회",
			description = "사용자 정보 조회"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "사용자 정보 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 200," +
											"\"success\": \"사용자 정보 조회에 성공했습니다.\"," +
											"\"data\": {" +
													"\"name\": \"John Doe\"," +
													"\"email\": \"john.doe@example.com\"," +
													"\"profile\": \"portrait_url_here\"," +
													"\"total_point\": 100," +
													"\"month_point\": 50" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "사용자 정보 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"사용자 정보 조회에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/mypage/info")
	public ResponseEntity<?> getMemberInfo();

	@Operation(
			summary = "사용자가 작성한 게시물 리스트",
			description = "Offset 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "사용자가 작성한 게시물 리스트 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\":true," +
											" \"status\":200," +
											" \"success\":\"사용자가 작성한 게시물 리스트를 불러왔습니다.\", " +
											"\"data\":{" +
											"\"content\": [" +
											"{\"id\": 2,\"writer_profile\": \"portrait_url_here\",\"writer_name\": \"John Doe\",\"content\": \"Content for board 2\",\"thumbnail\": \"\",\"tag\": true,\"like_on\": false,\"like_count\": 0,\"updated_at\": \"2024-06-07T13:51:45.62002\",\"comment_count\": 0}," +
											"{\"id\": 3,\"writer_profile\": \"portrait_url_here\",\"writer_name\": \"John Doe\",\"content\": \"Content for board 3\",\"thumbnail\": \"\",\"tag\": true,\"like_on\": false,\"like_count\": 0,\"updated_at\": \"2024-06-07T13:51:45.62002\",\"comment_count\": 0}" +
											"]" +
											"},\n" +
											"    \"pageable\": {\n" +
											"      \"pageNumber\": 0,\n" +
											"      \"pageSize\": 10,\n" +
											"      \"sort\": {\n" +
											"        \"empty\": true,\n" +
											"        \"unsorted\": true,\n" +
											"        \"sorted\": false\n" +
											"      },\n" +
											"      \"offset\": 0,\n" +
											"      \"paged\": true,\n" +
											"      \"unpaged\": false\n" +
											"    },\n" +
											"    \"last\": true,\n" +
											"    \"totalElements\": 2,\n" +
											"    \"totalPages\": 1,\n" +
											"    \"first\": true,\n" +
											"    \"size\": 10,\n" +
											"    \"number\": 0,\n" +
											"    \"sort\": {\n" +
											"      \"empty\": true,\n" +
											"      \"unsorted\": true,\n" +
											"      \"sorted\": false\n" +
											"    },\n" +
											"    \"numberOfElements\": 2,\n" +
											"    \"empty\": false\n" +
											"  }"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "사용자가 작성한 게시물 리스트 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"사용자가 작성한 게시물 리스트 조회에 실패했습니다.\"," +
											"\"data\": []" +
											"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러"),
	})
	@GetMapping("/mypage/posts")
	public ResponseEntity<?> getMemberPosts(@RequestParam(value = "page", defaultValue = "0") int page);
}
