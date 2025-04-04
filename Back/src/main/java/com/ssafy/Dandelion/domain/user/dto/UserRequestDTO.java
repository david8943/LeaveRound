package com.ssafy.Dandelion.domain.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserRequestDTO {

	private UserRequestDTO() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class AccountInfos {
		@JsonProperty("REC")
		private List<AccountInfo> rec;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class AccountInfo {
		private String bankCode;
		private String bankName;
		private String userName;
		private String accountNo;
		private String accountName;
		private String accountTypeCode;
		private String accountTypeName;
		private String accountCreatedDate;
		private String accountExpiryDate;
		private String dailyTransferLimit;
		private String oneTimeTransferLimit;
		private String accountBalance;
		private String lastTransactionDate;
		private String currency;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class DepositAccount {
		private String accountNo;
		private String accountBalance;
	}
}
