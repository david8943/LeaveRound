package com.ssafy.Dandelion.domain.autodonation.converter;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.SliceMoney;

public class AutoDonationConverter {
	public static AutoDonation toAutoDonation(Integer userId, RequestDTO.AutoDonationDTO request) {
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

	public static ResponseDTO.ReadAllAutoDonationDTO toRealAllAutoDonationDTO(
		List<ResponseDTO.AccountDTO> accountDTOList) {
		ResponseDTO.ReadAllAutoDonationDTO result = ResponseDTO.ReadAllAutoDonationDTO
			.builder()
			.activeAccounts(new ArrayList<>())
			.inactiveAccounts(new ArrayList<>())
			.build();

		accountDTOList.forEach(accountDTO -> {
			if (accountDTO.getIsActive()) {
				result.getActiveAccounts().add(accountDTO);
			} else {
				result.getInactiveAccounts().add(accountDTO);
			}
		});

		return result;
	}

	public static ResponseDTO.AccountDTO toAccountDTO(AutoDonation autoDonation, String organizationName) {
		return ResponseDTO.AccountDTO.builder()
			.autoDonationId(autoDonation.getAutoDonationId())
			.acountNo(autoDonation.getAccountNo())
			.bankName(Bank.fromBankCode(autoDonation.getBankCode()).getBankName())
			.sliceMoney(autoDonation.getSliceMoney().toString())
			.donationTime(autoDonation.getDonateTime().toString())
			.organizationName(organizationName)
			.isActive(autoDonation.isActive())
			.build();
	}
}
