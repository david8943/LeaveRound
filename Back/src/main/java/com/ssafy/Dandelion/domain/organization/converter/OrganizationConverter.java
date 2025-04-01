package com.ssafy.Dandelion.domain.organization.converter;

import java.util.List;

import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;

public class OrganizationConverter {

	private OrganizationConverter() {
		throw new IllegalStateException("OrganizationConverter Not Public Constructor");
	}

	public static OrganizationResponseDTO.OrganizationInfoList toOrganizationInfo
		(List<OrganizationResponseDTO.OrganizationInfo> organizationInfos) {
		return OrganizationResponseDTO.OrganizationInfoList.builder()
			.organizationInfos(organizationInfos)
			.build();
	}
}
