package com.ssafy.Dandelion.domain.autodonation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResponseDTO {

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
		Boolean isActive;
	}
}
