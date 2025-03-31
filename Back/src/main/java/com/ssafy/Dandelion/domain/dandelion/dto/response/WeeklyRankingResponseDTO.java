package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyRankingResponseDTO {

	private List<DandelionRankDTO> rankInfos;
	private MyDandelionRankDTO myRankInfo;
}
