package com.ssafy.Dandelion.domain.dandelion.service;

import java.util.List;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;
import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;

public interface DandelionService {

	// 사용자가 일반 민들레를 수집함
	Dandelion collectDandelion(Integer userId, Integer dandelionId);

	// 사용자가 황금 민들레를 수집함
	GoldDandelion collectGoldDandelion(Integer userId, Integer goldDandelionId);

	// 사용자의 위치 주변에 있는 일반 민들레 위치 정보 조회
	List<DandelionLocationResponseDTO> getNearbyDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO);

	// 사용자 주변의 황금 민들레 위치 정보를 조회
	List<GoldDandelionLocationResponseDTO> getNearbyGoldDandelions(DandelionLocationRequestDTO locationRequestDTO);

	// 사용자가 민들레를 기부함(황금 민들레 포함)
	void donateDandelions(Integer userId, DandelionDonationRequestDTO donationRequestDTO);

	// 사용자가 갖고 있는 일반 민들레 개수 조회
	int getUserDandelionCount(Integer userId);

	// 사용자가 보유한 황금 민들레 개수 조회
	int getUserGoldDandelionCount(Integer userId);

	// 주간 기부 랭킹 조회(황금 민들레는 일반 민들레 100개)
	WeeklyRankingResponseDTO getWeeklyRanking(Integer userId);

	// 월간 황금 민들레 수집 랭킹 조회
	MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId);
}
