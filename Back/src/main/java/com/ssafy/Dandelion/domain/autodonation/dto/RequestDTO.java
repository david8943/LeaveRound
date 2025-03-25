package com.ssafy.Dandelion.domain.autodonation.dto;

import com.ssafy.Dandelion.global.validation.annotation.ExistBank;
import com.ssafy.Dandelion.global.validation.annotation.ExistDonationTime;
import com.ssafy.Dandelion.global.validation.annotation.ExistSliceMoney;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RequestDTO {
	@Getter
	public static class CreateAutoDonationDTO {
		@NotBlank
		@ExistBank
		String bankName;

		@NotBlank
		String accountNo;

		@NotBlank
		@ExistSliceMoney
		String sliceMoney;

		@NotBlank
		@ExistDonationTime
		String donationTime;

		Integer organizationProjectId;
	}
}
