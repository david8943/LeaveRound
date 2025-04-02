package com.ssafy.Dandelion.domain.autodonation.service;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;

public interface AutoDonationService {
	void createAutoDonation(Integer userId, RequestDTO.AutoDonationDTO request);

	ResponseDTO.ReadAllAutoDonationDTO readAllAutoDonation(Integer userId);

	void changeActive(Integer userId, Integer autoDonationId);

	void deleteAutoDonation(Integer userId, Integer autoDonationId);

	void updateAutoDonation(Integer userId, Integer autoDonationId, RequestDTO.AutoDonationDTO request);

	ResponseDTO.ReadAutoDonationDTO readAutoDonation(Integer userId, Integer autoDonationId);

	ResponseDTO.AutoDonationTotalAccountDTO readTotalBalance(Integer userId);

	void executeAutoDonation(AutoDonation autoDonation);
}
