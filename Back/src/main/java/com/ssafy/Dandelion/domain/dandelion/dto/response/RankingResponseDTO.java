package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponseDTO {
	private List<RankDto> rankInfos; // 전체 민들레 순위
	private MyRankDto myRankInfo; // 내 민들레 순위

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RankDto {
		private Integer rank;
		private String name;
		private Integer donateCount;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MyRankDto {
		private Integer myRank;
		private String myName;
		private Integer myDonateCount;
	}
}
