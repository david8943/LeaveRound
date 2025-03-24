package com.ssafy.Dandelion.domain.dandelion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 위치 정보 요청 DTO
 * 사용자의 현재 위치를 전달하기 위한 클래스
 */
@Getter
@NoArgsConstructor
public class LocationRequest {
    @NotNull(message = "위도 정보는 필수입니다.")
    private BigDecimal myLatitude;

    @NotNull(message = "경도 정보는 필수입니다.")
    private BigDecimal myLongitude;
}
