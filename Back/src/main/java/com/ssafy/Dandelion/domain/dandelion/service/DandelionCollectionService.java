package com.ssafy.Dandelion.domain.dandelion.service;

import java.util.List;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;

public interface DandelionCollectionService {

    // 사용자 주변 민들레 위치 조회 및 재배치
    List<DandelionLocationResponseDTO> getPersonalDandelions(Integer userId,
                                                             DandelionLocationRequestDTO locationRequestDTO);

    // 이번 달 미수집 황금 민들레 전체 위치 조회
    List<GoldDandelionLocationResponseDTO> getUncollectedGoldDandelions();

    // 민들레 수집
    void collectDandelion(Integer userId, Integer dandelionId,
                          DandelionLocationRequestDTO locationRequestDTO);

    // 황금 민들레 수집
    void collectGoldDandelion(Integer userId, Integer goldDandelionId,
                              DandelionLocationRequestDTO locationRequestDTO);

    // 월별 황금 민들레 생성 및 관리 메소드
    void generateMonthlyGoldDandelions();
}
