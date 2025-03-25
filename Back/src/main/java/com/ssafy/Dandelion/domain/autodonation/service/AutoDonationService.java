package com.ssafy.Dandelion.domain.autodonation.service;

import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;

import jakarta.validation.Valid;

public interface AutoDonationService {
	void createAutoDonation(Integer userId, RequestDTO.CreateAutoDonationDTO request);

}
