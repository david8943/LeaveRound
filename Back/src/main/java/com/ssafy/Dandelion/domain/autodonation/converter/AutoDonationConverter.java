package com.ssafy.Dandelion.domain.autodonation.converter;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.SliceMoney;

public class AutoDonationConverter {
	public static AutoDonation toAutoDonation(Integer userId, RequestDTO.CreateAutoDonationDTO request) {
		return AutoDonation.builder()
			.organizationProjectId(request.getOrganizationProjectId())
			.userId(userId)
			.amountSum(0L)
			.sliceMoney(SliceMoney.valueOf(request.getSliceMoney()))
			.bankCode(Bank.fromBankName(request.getBankName()).getBankCode())
			.accountNo(request.getAccountNo())
			.donateTime(DonationTime.valueOf(request.getDonationTime()))
			.isActive(true)
			.build();

	}
}
