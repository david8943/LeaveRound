package com.ssafy.Dandelion.domain.autodonation.service;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;

import jakarta.validation.Valid;

public interface AutoDonationService {
	void createAutoDonation(Integer userId, RequestDTO.CreateAutoDonationDTO request);

	ResponseDTO.ReadAllAutoDonationDTO readAllAutoDonation(Integer userId);
}
