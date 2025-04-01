package com.ssafy.Dandelion.domain.organization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.Dandelion.domain.organization.dto.OrganizationRequestDTO;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;
import com.ssafy.Dandelion.domain.organization.entity.constant.ProjectCategory;
import com.ssafy.Dandelion.domain.organization.service.OrganizationService;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
public class OrganizationController {

	private final OrganizationService organizationService;

	@GetMapping
	public ApiResponse<OrganizationResponseDTO.OrganizationInfoList> getOrganizationInfos(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String projectCategory) {

		return ApiResponse.onSuccess(organizationService.getOrganizationInfos(
			new OrganizationRequestDTO.OrganizationSearchCondition(keyword,
				ProjectCategory.fromCategoryName(projectCategory))));
	}

	@GetMapping("/{organizationProjectId}")
	public ApiResponse<OrganizationResponseDTO.OrganizationInfo> getOrganizationInfo(
		@PathVariable Integer organizationProjectId
	) {
		return ApiResponse.onSuccess(organizationService.getOrganizationInfo(organizationProjectId));
	}
}
