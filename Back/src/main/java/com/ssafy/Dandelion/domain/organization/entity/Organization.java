package com.ssafy.Dandelion.domain.organization.entity;

import org.checkerframework.common.aliasing.qual.Unique;

import com.ssafy.Dandelion.global.audit.BaseTimeEntityWithUpdatedAt;

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
@Table(name = "organizations")
public class Organization extends BaseTimeEntityWithUpdatedAt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer organizationId;

	@Unique
	@Column(nullable = false)
	private String organizationName;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	//TODO: ENUM 설정 필요
	private String field;

	@Column(nullable = false)
	private boolean isAbnormal;

	@Column(nullable = false, length = 20)
	private String representative;
}
