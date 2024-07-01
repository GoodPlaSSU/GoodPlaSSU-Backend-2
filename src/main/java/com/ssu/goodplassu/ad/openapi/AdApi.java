package com.ssu.goodplassu.ad.openapi;

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

@Tag(name = "Ad", description = "광고 관련 API")
public interface AdApi {

	@Operation(
			summary = "광고 리스트",
			description = "Offset 기반 페이지네이션 10개씩 전송"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "광고 리스트 조회 성공",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{\n" +
											"  \"result\": true,\n" +
											"  \"status\": 200,\n" +
											"  \"success\": \"광고 리스트를 불러왔습니다.\",\n" +
											"  \"data\": {\n" +
											"    \"content\": [\n" +
											"      {\n" +
											"        \"id\": 2,\n" +
											"        \"image\": \"ad_image_2.jpg\",\n" +
											"        \"link\": \"ad_link_2.url\"\n" +
											"      },\n" +
											"      {\n" +
											"        \"id\": 1,\n" +
											"        \"image\": \"ad_image_1.jpg\",\n" +
											"        \"link\": \"ad_link_1.url\"\n" +
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
											"      \"paged\": true,\n" +
											"      \"unpaged\": false\n" +
											"    },\n" +
											"    \"last\": true,\n" +
											"    \"totalPages\": 1,\n" +
											"    \"totalElements\": 2,\n" +
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
					description = "광고 리스트 조회 실패",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ResponseDto.class),
							examples = @ExampleObject(value =
									"{" +
											"\"result\": false," +
											"\"status\": 400," +
											"\"success\": \"광고 리스트 조회에 실패했습니다.\"," +
											"\"data\": []" +
											"}"
							)
					)
			),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	@GetMapping("/")
	public ResponseEntity<?> getAds(@RequestParam(value = "page", defaultValue = "0") int page);
}
