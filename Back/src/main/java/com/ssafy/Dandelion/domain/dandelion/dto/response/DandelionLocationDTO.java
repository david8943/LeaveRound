package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class DandelionLocationDTO {
	// 민들레 고유 식별자
	private Integer dandelionId;

	// 민들레 위치의 위도 좌표;
	private BigDecimal latitude;

	// 민들레 위치의 경도 좌표
	private BigDecimal longitude;

	/**
	 * 민들레 위치가 유효한지 검증
	 * @return 위도와 경도가 모두 존재하면 true
	 */
	public boolean hasValidCoordinates() {
		return latitude != null && longitude != null;
	}

	/**
	 * 위치 좌표를 문자열로 반환
	 * @return "위도,경도" 형식의 문자열
	 */
	public String getCoordinatesAsString() {
		if (!hasValidCoordinates()) {
			return "위치 정보 없음";
		}
		return latitude + "," + longitude;
	}
}
