package com.ssafy.Dandelion.domain.organization.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ONGOING("진행중"),
	COMPLETED("종료");

	private final String statusName;
}
