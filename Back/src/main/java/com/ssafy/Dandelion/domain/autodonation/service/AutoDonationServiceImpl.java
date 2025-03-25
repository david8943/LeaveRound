package com.ssafy.Dandelion.domain.autodonation.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.autodonation.converter.AutoDonationConverter;
import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationProjectRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AutoDonationServiceImpl implements AutoDonationService{

	private final AutoDonationRepository autoDonationRepository;
	private final OrganizationProjectRepository organizationProjectRepository;

	@Transactional
	@Override
	public void createAutoDonation(Integer userId, RequestDTO.CreateAutoDonationDTO request) {
		// TODO: USER 인증 부분

		// TODO: ProjectID 인증 부분
		if (!organizationProjectRepository.existsById(request.getOrganizationId()))
			throw new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT);

		AutoDonation autoDonation = AutoDonationConverter.toAutoDonation(userId, request);
		autoDonationRepository.save(autoDonation);
	}
}
