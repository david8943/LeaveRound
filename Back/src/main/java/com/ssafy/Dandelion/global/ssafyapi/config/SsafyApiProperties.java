package com.ssafy.Dandelion.global.ssafyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SsafyApiProperties {
    @Value("${ssafy.api.baseUrl}")
    private String baseUrl;

    @Value("${ssafy.api.managerId}")
    private String managerId;

    @Value("${ssafy.api.key}")
    private String apiKey;

    // getter 메서드들

    public String createApiUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
