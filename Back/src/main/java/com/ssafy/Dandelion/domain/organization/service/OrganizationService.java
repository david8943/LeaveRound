package com.ssafy.Dandelion.domain.organization.service;

import com.ssafy.Dandelion.domain.organization.dto.OrganizationRequestDTO;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;

public interface OrganizationService {

	OrganizationResponseDTO.OrganizationInfoList getOrganizationInfos(
		OrganizationRequestDTO.OrganizationSearchCondition condition);
}
