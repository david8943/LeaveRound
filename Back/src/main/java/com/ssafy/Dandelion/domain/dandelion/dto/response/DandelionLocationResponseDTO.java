package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DandelionLocationResponseDTO {
	
	// 일반 민들레 고유 식별자
	private Integer dandelionId;

	// 민들레 위치의 위도 좌표;
	private BigDecimal latitude;

	// 민들레 위치의 경도 좌표
	private BigDecimal longitude;

	// Jackson 직렬화시 호출되는 getter 메소드를 오버라이드
	public BigDecimal getLatitude() {
		return latitude != null ? latitude.setScale(6, RoundingMode.HALF_UP) : null;
	}

	public BigDecimal getLongitude() {
		return longitude != null ? longitude.setScale(6, RoundingMode.HALF_UP) : null;
	}

}
