package com.ssafy.Dandelion.domain.autodonation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.autodonation.converter.AutoDonationConverter;
import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationProjectRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.BadRequestHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AutoDonationServiceImpl implements AutoDonationService{

	private final AutoDonationRepository autoDonationRepository;
	private final OrganizationRepository organizationRepository;
	private final OrganizationProjectRepository organizationProjectRepository;

	@Transactional
	@Override
	public void createAutoDonation(Integer userId, RequestDTO.CreateAutoDonationDTO request) {
		// TODO: USER 인증 부분

		// TODO: ProjectID 인증 부분
		if (!organizationProjectRepository.existsById(request.getOrganizationProjectId()))
			throw new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT);

		if (autoDonationRepository.existsByAccountNo(request.getAccountNo()))
			throw new BadRequestHandler(ErrorStatus.ALREADY_EXIST_AUTO_DONATION);

		AutoDonation autoDonation = AutoDonationConverter.toAutoDonation(userId, request);
		autoDonationRepository.save(autoDonation);
	}

	@Override
	public ResponseDTO.ReadAllAutoDonationDTO readAllAutoDonation(Integer userId) {
		// TODO: USER 인증 부분

		List<ResponseDTO.AccountDTO> accountDTOList = autoDonationRepository.findAllByUserId(userId).stream()
			.map(autoDonation -> {
				Integer organizationId = organizationProjectRepository.findById(autoDonation.getOrganizationProjectId())
					.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT))
					.getOrganizationId();

				String organizationName = organizationRepository.findById(organizationId)
					.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION))
					.getOrganizationName();

				return AutoDonationConverter.toAccountDTO(autoDonation, organizationName);
			})
			.toList();

		return AutoDonationConverter.toRealAllAutoDonationDTO(accountDTOList);
	}

}
