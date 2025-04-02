package com.ssafy.Dandelion.domain.dandelion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyGoldDandelionRankDTO {
	private String myName;
	private Integer myGoldCount;
}
