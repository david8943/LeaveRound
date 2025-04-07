package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class GoldDandelionLocationResponseDTO {
	private Integer goldDandelionId;
	private BigDecimal latitude;
	private BigDecimal longitude;

	// Jackson 직렬화시 호출되는 getter 메소드를 오버라이드
	public BigDecimal getLatitude() {
		return latitude != null ? latitude.setScale(6, RoundingMode.HALF_UP) : null;
	}

	public BigDecimal getLongitude() {
		return longitude != null ? longitude.setScale(6, RoundingMode.HALF_UP) : null;
	}
}
