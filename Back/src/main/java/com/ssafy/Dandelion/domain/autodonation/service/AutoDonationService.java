package com.ssafy.Dandelion.domain.autodonation.service;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;

public interface AutoDonationService {
	void createAutoDonation(Integer userId, RequestDTO.AutoDonationDTO request);

	ResponseDTO.ReadAllAutoDonationDTO readAllAutoDonation(Integer userId);

	void changeActive(Integer userId, Integer autoDonationId);

	void deleteAutoDonation(Integer userId, Integer autoDonationId);

	void UpdateAutoDonation(Integer userId, Integer autoDonationId, RequestDTO.AutoDonationDTO request);
}
