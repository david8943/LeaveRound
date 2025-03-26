package com.ssafy.Dandelion.domain.dandelion.entity;

import com.ssafy.Dandelion.global.audit.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {
	@Index(name = "idx_organization_project_id", columnList = "organizationProjectId"),
	@Index(name = "idx_user_id", columnList = "userId")
},
	name = "dandelion_donation_infos")
public class DandelionDonationInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dandelionDonationId;

	@Column(nullable = false)
	private Integer userId;

	@Column(nullable = false)
	private Integer organizationProjectId;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int useDandelionCount;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int useGoldDandelionCount;
}
