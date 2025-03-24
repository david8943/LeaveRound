package com.ssafy.Dandelion.global.ssafyapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequestDTO {
    private String apiKey;
    private String userId;
}
