package com.ssafy.Dandelion.global.ssafyapi.service;

import com.ssafy.Dandelion.global.ssafyapi.dto.response.ApiKeyResponseDTO;

public interface CommonSsafyApiService {

    // 발급, 최초로 API키를 받을 때 사용
    ApiKeyResponseDTO issueApiKey(String managerId);

    // 재발급, 기존 키를 확인하거나 새 키로 교체할 때 사용
    ApiKeyResponseDTO reissueApiKey(String managerId);
}
