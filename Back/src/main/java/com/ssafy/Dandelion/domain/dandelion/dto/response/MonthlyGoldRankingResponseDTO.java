package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class MonthlyGoldRankingResponseDTO {
	private List<GoldDandelionRankDTO> goldInfos;
	private MyGoldDandelionRankDTO myGoldInfo;
}
