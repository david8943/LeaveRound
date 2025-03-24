package com.ssafy.Dandelion.domain.autodonation.dto;

import org.checkerframework.checker.units.qual.N;

import com.ssafy.Dandelion.global.validation.annotation.ExistDonationTime;
import com.ssafy.Dandelion.global.validation.annotation.ExistSliceMoney;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RequestDTO {
	@Getter
	public static class CreateAutoDonationDTO {
		@NotBlank
		String bankName;

		Integer accountNo;

		@NotBlank
		@ExistSliceMoney
		String sliceMoney;

		@NotBlank
		@ExistDonationTime
		String donationTime;

		Integer organizationId;
	}
}
