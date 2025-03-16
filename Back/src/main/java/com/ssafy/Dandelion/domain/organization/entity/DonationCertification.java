package com.ssafy.Dandelion.domain.organization.entity;

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
})
public class DonationCertification extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer donationCertificationId;

	@Column(nullable = false)
	private Integer organizationProjectId;

	@Column(nullable = false)
	private Integer userId;

	@Column(nullable = false)
	private String imgUrl;
}
