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
public class GoldRankingResponseDTO {
	private List<GoldInfoDto> goldInfos;
	private MyGoldInfoDto myGoldInfo;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GoldInfoDto {
		private String name;
		private Integer goldCount;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MyGoldInfoDto {
		private String myName;
		private Integer mygoldCount;
	}
}
