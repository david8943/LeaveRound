package com.ssafy.Dandelion.domain.organization.repository;

import java.util.List;

import com.ssafy.Dandelion.domain.organization.dto.OrganizationRequestDTO;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;

public interface OrganizationCustomRepository {

	List<OrganizationResponseDTO.OrganizationInfo> findAllOrganizations(
		OrganizationRequestDTO.OrganizationSearchCondition condition);

	OrganizationResponseDTO.OrganizationInfo getOrganizationInfo(Integer organizationProjectId);
}
