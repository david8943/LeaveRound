package com.ssafy.Dandelion.domain.dandelion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyDandelionRankDTO {

	private Integer myRank;
	private String myName;
	private Integer myDonateCount;
}
