package com.ssafy.Dandelion.domain.dandelion.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DandelionDonationRequestDTO {

	// 기부처 Id (null 가능 - 랜덤 기부)
	private Integer projectId;
	// 기부할 일반 민들레 개수, 음수 안됨
	@Min(value = 0, message = "민들레 기부 개수는 0개 이상이어야 합니다.")
	private Integer dandelionCount;
	// 기부할 황금 민들레 개수, 음수 안됨
	@Min(value = 0, message = "황금 민들레 기부 개수는 0개 이상이어야 합니다.")
	private Integer goldDandelionCount;
}
