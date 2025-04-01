package com.ssafy.Dandelion.domain.dandelion.dto.response;

import java.math.BigDecimal;

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
}
