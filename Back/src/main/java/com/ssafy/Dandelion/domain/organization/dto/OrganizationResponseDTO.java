package com.ssafy.Dandelion.domain.organization.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrganizationResponseDTO {

	private OrganizationResponseDTO() {
		throw new IllegalStateException("OrganizationResponseDTO Not Public Constructor");
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrganizationInfoList {
		private List<OrganizationInfo> organizationInfos;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class OrganizationInfo {
		private Long organizationId;
		private String organizationName;
		private String address;
		private String representative;
		private String homepageUrl;
		private Long organizationProjectId;
		private Long goalAmount;
		private Long totalUseAmount;
		private Long currentAmount;
		private String projectTitle;
		private String projectContent;
		private String projectCategory;
		private String projectStatus;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private LocalDateTime endDatedAt;

		@QueryProjection
		public OrganizationInfo(Long organizationId, String organizationName, String address,
			String representative, String homepageUrl, Long organizationProjectId,
			Long goalAmount, Long totalUseAmount, Long currentAmount,
			String projectTitle, String projectContent, String projectCategory, String projectStatus,
			LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime endDatedAt) {
			this.organizationId = organizationId;
			this.organizationName = organizationName;
			this.address = address;
			this.representative = representative;
			this.homepageUrl = homepageUrl;
			this.organizationProjectId = organizationProjectId;
			this.goalAmount = goalAmount;
			this.totalUseAmount = totalUseAmount;
			this.currentAmount = currentAmount;
			this.projectTitle = projectTitle;
			this.projectContent = projectContent;
			this.projectCategory = projectCategory;
			this.projectStatus = projectStatus;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.endDatedAt = endDatedAt;
		}
	}
}
