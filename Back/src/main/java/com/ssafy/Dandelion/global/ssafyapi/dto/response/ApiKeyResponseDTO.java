package com.ssafy.Dandelion.global.ssafyapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
// api 서버에서 응답 받아올때
public class ApiKeyResponseDTO {
    private String managerId;
    private String apiKey;
    private String creationDate;
    private String expirationDate;
}
