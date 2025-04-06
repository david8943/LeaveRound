package com.ssafy.Dandelion.domain.dandelion.service;

import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;

public interface DandelionRankingService {

    // 주간 기부 랭킹 조회(일반 민들레 + 황금 민들레)
    WeeklyRankingResponseDTO getWeeklyRanking(Integer userId);

    // 월간 황금 민들레 수집 랭킹
    MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId);
}
