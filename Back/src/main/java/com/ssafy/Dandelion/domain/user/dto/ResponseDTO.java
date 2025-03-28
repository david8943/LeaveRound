package com.ssafy.Dandelion.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.Dandelion.global.config.SsafyApiProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseDTO {

	private ResponseDTO() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AccountCreateDTO {

		@JsonProperty("Header")
		private SsafyApiProperties.SsafyApiHeader header;
		private String accountTypeUniqueNo;

	}
}
