package com.ssafy.Dandelion.domain.dandelion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DandelionRankDTO {

	private Integer rank;
	private String name;
	private Integer donateCount;
}
