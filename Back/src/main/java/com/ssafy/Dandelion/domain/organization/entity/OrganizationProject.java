package com.ssafy.Dandelion.domain.organization.entity;

import java.time.LocalDateTime;

import org.checkerframework.common.aliasing.qual.Unique;

import com.ssafy.Dandelion.global.audit.BaseTimeEntityWithUpdatedAt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "idx_organization_id", columnList = "organizationId"))
public class OrganizationProject extends BaseTimeEntityWithUpdatedAt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer organizationProjectId;

	@Unique
	@Column(nullable = false)
	private Integer organizationId;

	@Column(nullable = false)
	private Long goalAmount;

	@Column(nullable = false)
	private Long currentAmount;

	@Column(nullable = false)
	//TODO: ENUM 설정 필요
	private String bankCode;

	@Column(nullable = false, length = 20)
	private String accountNo;

	@Column(nullable = true)
	//TODO: ENUM 설정 필요
	private String projectCategory;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column
	private LocalDateTime endDatedAt;
}
