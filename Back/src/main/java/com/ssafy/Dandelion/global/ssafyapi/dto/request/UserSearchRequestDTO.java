package com.ssafy.Dandelion.global.ssafyapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserSearchRequestDTO {
    private String apiKey;
    private String userId;
}
