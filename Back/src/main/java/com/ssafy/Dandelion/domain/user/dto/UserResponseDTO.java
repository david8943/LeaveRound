package com.ssafy.Dandelion.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.Dandelion.global.config.SsafyApiProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {

	private UserResponseDTO() {
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

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AccountDTO {
		private Integer autoDonationId;
		private String bankName;
		private String accountNo;
		private String accountMoney;
		private String accountStatus;

		public void setAccountStatus(String accountStatus) {
			this.accountStatus = accountStatus;
		}

		public void setAutoDonationId(int autoDonationId) {
			this.autoDonationId = autoDonationId;
		}
	}

}
