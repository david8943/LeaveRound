package com.ssafy.Dandelion.domain.dandelion.service;

import java.util.List;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;

public interface DandelionService {

	// 민들레 기부(황금 민들레 + 일반 민들레)
	void donateDandelions(Integer userId, DandelionDonationRequestDTO donationRequestDTO);

	// 사용자가 보유한 총 일반 민들레 개수 조회(유저)
	int getTotalDandelionCount(Integer userId);

	// 사용자가 보유한 총 황금 민들레 개수 조회(유저)
	int getTotalGoldDandelionCount(Integer userId);

	// 사용자가 기부 가능한 일반 민들레 개수 조회
	int getAvailableDandelionCount(Integer userId);

	// 사용자가 기부 가능한 황금 민들레 개수 조회
	int getAvailableGoldDandelionCount(Integer userId);

	// 주간 기부 랭킹 조회(일반 민들레 + 황금 민들레)
	WeeklyRankingResponseDTO getWeeklyRanking(Integer userId);

	// 월간 황금 민들레 수집 랭킹
	MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId);

	// 일반 민들레 위치 조회 및 재배치
	List<DandelionLocationResponseDTO> getAndRelocatePersonalDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO);

	// 이번 달 미수집 황금 민들레 전체 위치 조회
	List<GoldDandelionLocationResponseDTO> getMonthlyUncollectedGoldDandelions();

	// 민들레 수집 (거리 체크 포함)
	void collectDandelionWithDistanceCheck(Integer userId, Integer dandelionId,
		DandelionLocationRequestDTO locationRequestDTO);

	// 황금 민들레 수집 (거리 체크 포함)
	void collectGoldDandelionWithDistanceCheck(Integer userId, Integer goldDandelionId,
		DandelionLocationRequestDTO locationRequestDTO);

	// 월별 황금 민들레 생성 및 관리 메소드
	void generateMonthlyGoldDandelions();
}
