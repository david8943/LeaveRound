package com.ssafy.Dandelion.domain.autodonation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ResponseDTO {

	private ResponseDTO() {
		throw new IllegalStateException("ResponseDTO Not Public Constructor");
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReadAllAutoDonationDTO {
		List<AccountDTO> activeAccounts;
		List<AccountDTO> inactiveAccounts;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AccountDTO {
		Integer autoDonationId;
		String bankName;
		String acountNo;
		String sliceMoney;
		String donationTime;
		String organizationName;
		String accountBalance;
		Long amountSum;
		Boolean isActive;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReadAutoDonationDTO {
		Integer autoDonationId;
		String bankName;
		String acountNo;
		String sliceMoney;
		String donationTime;
		String organizationName;
		Boolean isActive;
		Long amountSum;
		List<AutoDonationInfoDTO> autoDonationInfos;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AutoDonationInfoDTO {
		Integer autoDonationInfoId;
		Long transactionBalance;
		LocalDateTime createTime;
		String organizationName;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AutoDonationTotalAccountDTO {
		Long totalAccount;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class TransactionInfos {
		@JsonProperty("REC")
		private List<ResponseDTO.TransactionInfo> rec;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class TransactionInfo {
		private String transactionUniqueNo;
		private String accountNo;
		private String transactionDate;
		private String transactionType;
		private String transactionTypeName;
		private String transactionAccountNo;
	}
}
