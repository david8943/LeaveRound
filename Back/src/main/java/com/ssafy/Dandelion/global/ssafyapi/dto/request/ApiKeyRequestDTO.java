package com.ssafy.Dandelion.global.ssafyapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
// api 서버로 managerId 담아서 api 키 발급 받을 때 보내는 요청
public class ApiKeyRequestDTO {
    private String managerId;
}
