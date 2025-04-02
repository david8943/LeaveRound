package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.math.BigDecimal;

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

}
