package com.ssafy.Dandelion.domain.dandelion.service;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;

public interface DandelionDonationService {

    // 사용자가 민들레 기부(황금 민들레 , 일반 민들레)
    void donateDandelions(Integer userId, DandelionDonationRequestDTO donationRequestDTO);

    // 사용자가 기부 가능한 일반 민들레 개수 조회
    int getAvailableDandelionCount(Integer userId);

    // 사용자가 기부 가능한 황금 민들레 개수 조회
    int getAvailableGoldDandelionCount(Integer userId);
}
