package com.ssafy.Dandelion.domain.autodonation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.autodonation.converter.AutoDonationConverter;
import com.ssafy.Dandelion.domain.autodonation.dto.RequestDTO;
import com.ssafy.Dandelion.domain.autodonation.dto.ResponseDTO;
import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationInfoRepository;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationProjectRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationRepository;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.BadRequestHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AutoDonationServiceImpl implements AutoDonationService {

	private final AutoDonationRepository autoDonationRepository;
	private final AutoDonationInfoRepository autoDonationInfoRepository;
	private final OrganizationRepository organizationRepository;
	private final OrganizationProjectRepository organizationProjectRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public void createAutoDonation(Integer userId, RequestDTO.AutoDonationDTO request) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

		if (!organizationProjectRepository.existsById(request.getOrganizationProjectId()))
			throw new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT);

		if (autoDonationRepository.existsByAccountNo(request.getAccountNo()))
			throw new BadRequestHandler(ErrorStatus.ALREADY_EXIST_AUTO_DONATION);

		AutoDonation autoDonation = AutoDonationConverter.toAutoDonation(userId, request);
		autoDonationRepository.save(autoDonation);
	}

	@Override
	public ResponseDTO.ReadAllAutoDonationDTO readAllAutoDonation(Integer userId) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

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

	@Transactional
	@Override
	public void changeActive(Integer userId, Integer autoDonationId) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

		AutoDonation target = autoDonationRepository.findById(autoDonationId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_AUTO_DONATION));

		target.changeActive();
		autoDonationRepository.save(target);
	}

	@Transactional
	@Override
	public void deleteAutoDonation(Integer userId, Integer autoDonationId) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

		AutoDonation target = autoDonationRepository.findById(autoDonationId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_AUTO_DONATION));

		autoDonationInfoRepository.findAllByAutoDonationId(target.getAutoDonationId())
			.forEach(autoDonationInfoRepository::delete);

		autoDonationRepository.delete(target);
	}

	@Transactional
	@Override
	public void updateAutoDonation(Integer userId, Integer autoDonationId, RequestDTO.AutoDonationDTO request) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

		AutoDonation target = autoDonationRepository.findById(autoDonationId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_AUTO_DONATION));

		if (!organizationProjectRepository.existsById(request.getOrganizationProjectId()))
			throw new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT);

		if (autoDonationRepository.existsByAccountNo(request.getAccountNo()))
			throw new BadRequestHandler(ErrorStatus.ALREADY_EXIST_AUTO_DONATION);

		target.updateAutoDonation(AutoDonationConverter.toAutoDonation(userId, request));
		autoDonationRepository.save(target);
	}

	@Override
	public ResponseDTO.ReadAutoDonationDTO readAutoDonation(Integer userId, Integer autoDonationId) {
		if (userRepository.existsById(userId))
			throw new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND);

		List<ResponseDTO.AutoDonationInfoDTO> autoDonationInfoDTOList = autoDonationInfoRepository.findAllByAutoDonationId(
				autoDonationId).stream()
			.map(autoDonationInfo -> {
				Integer organizationId = organizationProjectRepository.findById(
						autoDonationInfo.getOrganizationProjectId())
					.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT))
					.getOrganizationId();

				String organizationName = organizationRepository.findById(organizationId)
					.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION))
					.getOrganizationName();

				return AutoDonationConverter.toAutoDonationInfoDTO(autoDonationInfo, organizationName);
			})
			.toList();

		AutoDonation target = autoDonationRepository.findById(autoDonationId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_AUTO_DONATION));

		Integer organizationId = organizationProjectRepository.findById(target.getAutoDonationId())
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION_PROJECT))
			.getOrganizationId();

		String organizationName = organizationRepository.findById(organizationId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_ORGANIZATION))
			.getOrganizationName();

		return AutoDonationConverter.toAutoDonationDTO(target, autoDonationInfoDTOList, organizationName);
	}

}
