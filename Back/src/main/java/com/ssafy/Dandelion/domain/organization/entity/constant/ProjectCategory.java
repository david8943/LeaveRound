package com.ssafy.Dandelion.domain.organization.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectCategory {

	ENVIRONMENT("환경"),
	HEALTHCARE("보건"),
	EDUCATION("교육"),
	OTHER("기타");

	private final String categoryName;
}
