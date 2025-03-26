package com.ssafy.Dandelion.global.ssafyapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;
import com.ssafy.Dandelion.global.ssafyapi.config.SsafyApiProperties;
import com.ssafy.Dandelion.global.ssafyapi.dto.request.ApiKeyRequestDTO;
import com.ssafy.Dandelion.global.ssafyapi.dto.response.ApiKeyResponseDTO;
import com.ssafy.Dandelion.global.ssafyapi.dto.response.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonSsafyApiServiceImpl implements CommonSsafyApiService {

    private final RestTemplate restTemplate; // RestTemplate 객체, 외부 API 호출을 위해 사용
    private final ObjectMapper objectMapper; // JSON 데이터를 객체로 변환
    private final SsafyApiProperties ssafyApiProperties; // SsafyApiProperties 객체, API URL 등을 담고 있음

    /**
     * API 키를 발급하는 메소드
     *
     * @param managerId API 키를 발급받을 관리자 ID
     * @return 발급된 API 키 정보(ApiKeyResponseDTO)
     */
    @Override
    public ApiKeyResponseDTO issueApiKey(String managerId) {
        String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/edu/app/issuedApiKey");

        // API 요청 DTO 객체 생성
        ApiKeyRequestDTO requestDTO = ApiKeyRequestDTO.builder()
                .managerId(managerId) // 관리자의 ID를 설정
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApiKeyRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        try {
            // POST 방식으로 API 요청을 보내고 응답을 ApiKeyResponseDTO 객체로 받음
            ResponseEntity<ApiKeyResponseDTO> response = restTemplate.postForEntity(
                    url, request, ApiKeyResponseDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("API key 발급 오류 발생: {}", e.getResponseBodyAsString());

            try {
                // 오류 응답을 ErrorResponseDTO로 변환하여 처리
                ErrorResponseDTO errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponseDTO.class);
                throw new GeneralException(ErrorStatus._BAD_REQUEST);
            } catch (Exception ex) {
                throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
                // 예외 처리 중 오류가 발생하면 내부 서버 오류로 처리
            }
        }
    }

    /**
     * API 키를 재발급하는 메소드
     *
     * @param managerId API 키를 재발급받을 관리자 ID
     * @return 재발급된 API 키 정보(ApiKeyResponseDTO)
     */
    @Override
    public ApiKeyResponseDTO reissueApiKey(String managerId) {
        String url = ssafyApiProperties.createApiUrl("/ssafy/api/v1/edu/app/reIssuedApiKey");

        // API 요청 DTO 객체 생성
        ApiKeyRequestDTO requestDTO = ApiKeyRequestDTO.builder()
                .managerId(managerId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ApiKeyRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        try {
            // POST 방식으로 API 요청을 보내고 응답을 ApiKeyResponseDTO 객체로 받음
            ResponseEntity<ApiKeyResponseDTO> response = restTemplate.postForEntity(
                    url, request, ApiKeyResponseDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("API key 재발급 오류 발생: {}", e.getResponseBodyAsString());
            // 오류 메시지를 로그에 기록

            try {
                // 오류 응답을 ErrorResponseDTO로 변환하여 처리
                ErrorResponseDTO errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponseDTO.class);
                throw new GeneralException(ErrorStatus._BAD_REQUEST);
            } catch (Exception ex) {
                throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        }
    }
}
