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
	@GetMapping("/info")
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
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/posts")
	public ResponseEntity<?> getMemberPosts(@RequestParam(value = "page", defaultValue = "0") int page);

	@Operation(
			summary = "사용자가 댓글을 작성한 게시물 리스트",
			description = "Offset 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "사용자가 댓글을 작성한 게시물 리스트 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{\n" +
											"  \"result\": true,\n" +
											"  \"status\": 200,\n" +
											"  \"success\": \"사용자가 댓글을 작성한 게시물 리스트를 불러왔습니다.\",\n" +
											"  \"data\": {\n" +
											"    \"content\": [\n" +
											"      {\n" +
											"        \"id\": 20,\n" +
											"        \"writer_profile\": \"portrait_url_here\",\n" +
											"        \"writer_name\": \"Bob Brown\",\n" +
											"        \"content\": \"Content for board 20\",\n" +
											"        \"thumbnail\": \"\",\n" +
											"        \"tag\": true,\n" +
											"        \"like_on\": false,\n" +
											"        \"like_count\": 0,\n" +
											"        \"updated_at\": \"2024-06-12T10:24:56.381941\",\n" +
											"        \"comment_count\": 1\n" +
											"      },\n" +
											"      {\n" +
											"        \"id\": 15,\n" +
											"        \"writer_profile\": \"portrait_url_here\",\n" +
											"        \"writer_name\": \"Alice Johnson\",\n" +
											"        \"content\": \"Content for board 15\",\n" +
											"        \"thumbnail\": \"\",\n" +
											"        \"tag\": false,\n" +
											"        \"like_on\": false,\n" +
											"        \"like_count\": 0,\n" +
											"        \"updated_at\": \"2024-06-12T10:24:47.83209\",\n" +
											"        \"comment_count\": 4\n" +
											"      }\n" +
											"    ],\n" +
											"    \"pageable\": {\n" +
											"      \"pageNumber\": 0,\n" +
											"      \"pageSize\": 10,\n" +
											"      \"sort\": {\n" +
											"        \"empty\": true,\n" +
											"        \"sorted\": false,\n" +
											"        \"unsorted\": true\n" +
											"      },\n" +
											"      \"offset\": 0,\n" +
											"      \"unpaged\": false,\n" +
											"      \"paged\": true\n" +
											"    },\n" +
											"    \"last\": true,\n" +
											"    \"totalElements\": 2,\n" +
											"    \"totalPages\": 1,\n" +
											"    \"first\": true,\n" +
											"    \"size\": 10,\n" +
											"    \"number\": 0,\n" +
											"    \"sort\": {\n" +
											"      \"empty\": true,\n" +
											"      \"sorted\": false,\n" +
											"      \"unsorted\": true\n" +
											"    },\n" +
											"    \"numberOfElements\": 2,\n" +
											"    \"empty\": false\n" +
											"  }\n" +
											"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "사용자가 댓글을 작성한 게시물 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"사용자가 댓글을 작성한 게시물 리스트 조회에 실패했습니다.\"," +
											"\"data\": []" +
											"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/comments/posts")
	public ResponseEntity<?> getMemberCommentPosts(@RequestParam(value = "page", defaultValue = "0") int page);

	@Operation(
			summary = "사용자가 좋아요를 누른 게시물 리스트",
			description = "Offset 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "사용자가 좋아요를 누른 게시물 리스트 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{\n" +
											"  \"result\": true,\n" +
											"  \"status\": 200,\n" +
											"  \"success\": \"사용자가 좋아요를 누른 게시물 리스트를 불러왔습니다.\",\n" +
											"  \"data\": {\n" +
											"    \"content\": [\n" +
											"      {\n" +
											"        \"id\": 16,\n" +
											"        \"writer_profile\": \"portrait_url_here\",\n" +
											"        \"writer_name\": \"Bob Brown\",\n" +
											"        \"content\": \"Content for board 16\",\n" +
											"        \"thumbnail\": \"\",\n" +
											"        \"tag\": true,\n" +
											"        \"like_on\": true,\n" +
											"        \"like_count\": 1,\n" +
											"        \"updated_at\": \"2024-06-20T16:47:55.3285\",\n" +
											"        \"comment_count\": 0\n" +
											"      },\n" +
											"      {\n" +
											"        \"id\": 20,\n" +
											"        \"writer_profile\": \"portrait_url_here\",\n" +
											"        \"writer_name\": \"Bob Brown\",\n" +
											"        \"content\": \"Content for board 20\",\n" +
											"        \"thumbnail\": \"\",\n" +
											"        \"tag\": true,\n" +
											"        \"like_on\": true,\n" +
											"        \"like_count\": 1,\n" +
											"        \"updated_at\": \"2024-06-20T16:47:55.3285\",\n" +
											"        \"comment_count\": 1\n" +
											"      },\n" +
											"    ],\n" +
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
											"    \"totalElements\": 8,\n" +
											"    \"totalPages\": 1,\n" +
											"    \"first\": true,\n" +
											"    \"size\": 10,\n" +
											"    \"number\": 0,\n" +
											"    \"sort\": {\n" +
											"      \"empty\": true,\n" +
											"      \"unsorted\": true,\n" +
											"      \"sorted\": false\n" +
											"    },\n" +
											"    \"numberOfElements\": 8,\n" +
											"    \"empty\": false\n" +
											"  }\n" +
											"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "사용자가 좋아요를 누른 게시물 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"사용자가 좋아요를 누른 게시물 리스트 조회에 실패했습니다.\"," +
											"\"data\": []" +
											"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/likes/posts")
	public ResponseEntity<?> getMemberLikePosts(@RequestParam(value = "page", defaultValue = "0") int page);
}
