package com.ssafy.Dandelion.domain.autodonation.entity;

import com.ssafy.Dandelion.global.audit.BaseTimeEntityWithUpdatedAt;

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
})
public class AutoDonation extends BaseTimeEntityWithUpdatedAt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer autoDonationId;

	@Column(nullable = false)
	private Integer organizationProjectId;

	@Column(nullable = false)
	private Integer userId;

	@Column(nullable = false)
	private Long amountSum;

	@Column(nullable = false)
	//TODO: ENUM 설정 필요
	private String sliceMoney;

	@Column(nullable = false)
	//TODO: ENUM 설정 필요
	private String bankCode;

	@Column(nullable = false, length = 20)
	private String accountNo;

	@Column(nullable = false)
	//TODO: ENUM 설정 필요
	private String donateTime;

	@Column(nullable = false)
	private boolean isActive;
}
