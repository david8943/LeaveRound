package com.ssafy.Dandelion.domain.autodonation.entity;

import com.ssafy.Dandelion.global.audit.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
	@Index(name = "idx_auto_donation_id", columnList = "autoDonationId"),
	@Index(name = "idx_organization_project_id", columnList = "organizationProjectId"),
	@Index(name = "idx_user_id", columnList = "userId")},
	name = "auto_donation_infos")
public class AutoDonationInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer autoDonationInfoId;

	@Column(nullable = false)
	private Integer autoDonationId;

	@Column(nullable = false)
	private Integer organizationProjectId;

	@Column(nullable = false)
	private Integer userId;

	@Column(nullable = false)
	private Long transactionBalance;
}
