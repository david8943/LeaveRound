package com.ssafy.Dandelion.domain.autodonation.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonationInfo;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.Bank;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.SliceMoney;

public class AutoDonationConverter {

	private AutoDonationConverter() {
		throw new IllegalStateException("AutoDonationConverter Not Public Constructor");
	}

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

	public static ResponseDTO.AutoDonationInfoDTO toAutoDonationInfoDTO(AutoDonationInfo autoDonationInfo,
		String organizationName) {
		return ResponseDTO.AutoDonationInfoDTO.builder()
			.autoDonationInfoId(autoDonationInfo.getAutoDonationInfoId())
			.transactionBalance(autoDonationInfo.getTransactionBalance())
			.createTime(autoDonationInfo.getCreatedAt())
			.organizationName(organizationName)
			.build();
	}

	public static ResponseDTO.ReadAutoDonationDTO toAutoDonationDTO(AutoDonation autoDonation,
		List<ResponseDTO.AutoDonationInfoDTO> autoDonationInfoDTOList,
		String organizationName) {

		Long totalBalance = autoDonationInfoDTOList.stream()
			.map(ResponseDTO.AutoDonationInfoDTO::getTransactionBalance)
			.filter(Objects::nonNull)
			.reduce(0L, Long::sum);

		return ResponseDTO.ReadAutoDonationDTO.builder()
			.autoDonationId(autoDonation.getAutoDonationId())
			.bankName(Bank.fromBankCode(autoDonation.getBankCode()).toString())
			.acountNo(autoDonation.getAccountNo())
			.sliceMoney(autoDonation.getSliceMoney().toString())
			.donationTime(autoDonation.getDonateTime().toString())
			.isActive(autoDonation.isActive())
			.totalBalance(totalBalance)
			.organizationName(organizationName)
			.autoDonationInfos(autoDonationInfoDTOList)
			.build();

	}

	public static ResponseDTO.AutoDonationTotalAccountDTO totalAccountDTO(List<AutoDonation> autoDonationList) {
		long sum = autoDonationList.stream()
			.mapToLong(AutoDonation::getAmountSum)
			.reduce(0L, Long::sum);

		return ResponseDTO.AutoDonationTotalAccountDTO.builder()
			.totalAccount(sum)
			.build();

	}
}
