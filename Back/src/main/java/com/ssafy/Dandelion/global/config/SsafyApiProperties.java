package com.ssafy.Dandelion.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
// SSAFY API 관련 설정 값을 관리
public class SsafyApiProperties {

    @Value("${ssafy.api.baseUrl}")
    private String baseUrl;

    @Value("${ssafy.api.managerId}")
    private String managerId;

    @Value("${ssafy.api.key}")
    private String apiKey;

    public String createApiUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
