package com.ssafy.Dandelion.global.ssafyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
// SSAFY API 관련 설정 값을 관리
public class SsafyApiProperties {
    @Value("${ssafy.api.baseUrl}")
    private String baseUrl;

    @Value("${ssafy.api.managerId}")
    private String managerId;

    @Value("${ssafy.api.key}")
    private String apiKey;

    // getter 메서드들
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String createApiUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
