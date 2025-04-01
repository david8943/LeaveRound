package com.ssafy.Dandelion.domain.organization.dto;

import com.ssafy.Dandelion.domain.organization.entity.constant.ProjectCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class OrganizationRequestDTO {

	private OrganizationRequestDTO() {
		throw new IllegalStateException("OrganizationRequestDTO Not Public Constructor");
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	@AllArgsConstructor
	public static class OrganizationSearchCondition {
		private String keyword;
		private ProjectCategory projectCategory;
	}
}
