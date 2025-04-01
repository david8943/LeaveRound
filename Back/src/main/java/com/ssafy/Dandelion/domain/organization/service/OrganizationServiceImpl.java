package com.ssafy.Dandelion.domain.organization.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.organization.converter.OrganizationConverter;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationRequestDTO;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

	private final OrganizationProjectRepository organizationProjectRepository;

	public OrganizationResponseDTO.OrganizationInfoList getOrganizationInfos(
		OrganizationRequestDTO.OrganizationSearchCondition condition) {
		List<OrganizationResponseDTO.OrganizationInfo> organizationInfos = organizationProjectRepository.findAllOrganizations(
			condition);

		return OrganizationConverter.toOrganizationInfo(organizationInfos);
	}
}
