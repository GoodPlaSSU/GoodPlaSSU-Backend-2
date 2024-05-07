package com.ssu.goodplassu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
	private boolean result;
	private int status;
	private String success;
	private T data;

	public ResponseDto(int status, String success, T data) {
		this.result = status >= 200 && status < 400;
		this.status = status;
		this.success = success;
		this.data = data;
	}
}