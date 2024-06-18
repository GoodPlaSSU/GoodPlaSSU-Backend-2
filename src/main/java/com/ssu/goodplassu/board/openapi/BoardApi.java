package com.ssu.goodplassu.board.openapi;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.request.PostModifyRequest;
import com.ssu.goodplassu.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Board", description = "게시판 관련 API")
public interface BoardApi {

	@Operation(
			summary = "게시판 게시물 리스트",
			description = "Offset 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "게시물 리스트 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\":true," +
											" \"status\":200," +
											" \"success\":\"게시물 리스트를 불러왔습니다.\", " +
											"\"data\":{" +
													"\"content\": [" +
															"{\"id\": 2,\"writer_profile\": \"portrait_url_here\",\"writer_name\": \"John Doe\",\"content\": \"Content for board 2\",\"thumbnail\": \"\",\"like_on\": false,\"like_count\": 0,\"updated_at\": \"2024-06-07T13:51:45.62002\",\"comment_count\": 0}," +
															"{\"id\": 3,\"writer_profile\": \"portrait_url_here\",\"writer_name\": \"John Doe\",\"content\": \"Content for board 3\",\"thumbnail\": \"\",\"like_on\": false,\"like_count\": 0,\"updated_at\": \"2024-06-07T13:51:45.62002\",\"comment_count\": 0}" +
													"]" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "게시물 리스트 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 리스트 조회에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러"),
	})
	@GetMapping("/")
	public ResponseEntity<?> getBoards(
			@RequestParam(name = "tag") final int tagAsInt,
			@RequestParam(value = "page", defaultValue = "0") int page
	);

	@Operation(
			summary = "게시물 조회",
			description = "게시물 아이디로 게시물 내용 조회"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "게시물 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											" \"status\": 200," +
											" \"success\": \"게시물을 조회했습니다.\", " +
											"\"data\": {" +
													"\"id\": 1," +
													"\"writer_id\": 1," +
													"\"writer_name\": \"John Doe\"," +
													"\"writer_profile\": \"portrait_url_here\"," +
													"\"content\": \"Content for board 1\"," +
													"\"images\": {" +
															"\"imageUrls\": []" +
													"}," +
													"\"updated_at\": \"2024-06-12T16:37:49.896831\"," +
													"\"like_count\": 0," +
													"\"comments\": []," +
													"\"like_on\": false" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "게시물 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 404," +
											"\"success\": \"게시물 조회에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/{postId}")
	public ResponseEntity<?> getBoardById(
			@PathVariable(value = "postId") final Long postId
	);

	@Operation(
			summary = "게시물 생성",
			description = "게시판에 새 게시물 생성 | " +
					"게시글 데이터는 JSON 형식으로 첨부해야 함\n" +
					"```json\n" +
					"{\n" +
					"  \"content\": \"새 게시글 작성 테스트\",\n" +
					"  \"tag\": 0\n" +
					"}\n" +
					"```\n\n" +
					"위 형식의 JSON 데이터를 파일로 저장한 후, Swagger UI를 통한 API 테스트 시 'postCreateRequest' 파트에 해당 파일 첨부\n" +
					"'images' 파트에는 사진 파일을 첨부할 수 있음. 필수는 아니며, 여러 파일을 첨부할 수 있음"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "게시물 생성 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 201," +
											"\"success\": \"게시물을 생성했습니다.\"," +
											"\"data\": {" +
													"\"id\": 1" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "게시물 생성 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 생성에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createPost(
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostCreateRequest postCreateRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	);

	@Operation(
			summary = "게시물 수정",
			description = "기존 게시물 수정 | " +
					"게시글 데이터는 JSON 형식으로 첨부해야 함\n" +
					"```json\n" +
					"{\n" +
					"  \"content\": \"새 게시글 수정 테스트\",\n" +
					"  \"tag\": 0,\n" +
					"  \"delete_images\":\n" +
					"  [\n" +
					"\t\t\"delete_image_1.jpg\",\n" +
					"\t\t\"delete_image_2.jpg\"\n" +
					"  ]\n" +
					"}\n" +
					"```\n\n" +
					"위 형식의 JSON 데이터를 파일로 저장한 후, Swagger UI를 통한 API 테스트 시 'postModifyRequest' 파트에 해당 파일 첨부\n" +
					"'images' 파트에는 사진 파일을 첨부할 수 있음. 필수는 아니며, 여러 파일을 첨부할 수 있음"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "게시물 수정 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 200," +
											"\"success\": \"게시물을 수정했습니다.\"," +
											"\"data\": {" +
											"\"id\": 1" +
											"}" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "게시물 등록 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 수정에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@PostMapping(path = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> modifyPost(
			@PathVariable("postId") final Long postId,
			@RequestPart @Parameter(schema = @Schema(type = "string", format = "binary")) @Valid final PostModifyRequest postModifyRequest,
			@RequestPart(value = "images", required = false) final List<MultipartFile> multipartFiles
	);

	@Operation(
			summary = "게시물 삭제",
			description = "게시물 삭제"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "게시물 삭제 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": true," +
											"\"status\": 204," +
											"\"success\": \"게시물을 삭제했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "게시물 등록 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"게시물 삭제에 실패했습니다.\"," +
											"\"data\": []" +
									"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@DeleteMapping(path = "/{postId}")
	public ResponseEntity<?> deletePost(
			@PathVariable("postId") final Long postId
	);
}
