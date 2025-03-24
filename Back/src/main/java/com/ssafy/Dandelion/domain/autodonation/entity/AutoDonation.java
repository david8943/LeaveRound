package com.ssafy.Dandelion.domain.autodonation.entity;

import com.ssafy.Dandelion.domain.autodonation.entity.constant.DonationTime;
import com.ssafy.Dandelion.domain.autodonation.entity.constant.SliceMoney;
import com.ssafy.Dandelion.global.audit.BaseTimeEntityWithUpdatedAt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SliceMoney sliceMoney;

	@Column(nullable = false)
	private String bankCode;

	@Column(nullable = false, length = 20)
	private String accountNo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DonationTime donateTime;

	@Column(nullable = false)
	private boolean isActive;
}
