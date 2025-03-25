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
		List<AmountDTO> activeAmounts;
		List<AmountDTO> inactiveAmounts;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AmountDTO {
		Integer autoDonationId;
		String bankName;
	}
}
