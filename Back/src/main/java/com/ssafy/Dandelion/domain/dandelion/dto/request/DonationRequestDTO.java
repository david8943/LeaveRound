package com.ssafy.Dandelion.domain.dandelion.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DonationRequestDTO {
    /**
     * 기부처 프로젝트 ID
     * null인 경우 랜덤 기부를 의미
     */
    private Integer projectId;

    /**
     * 기부할 일반 민들레 개수
     * 음수안됨
     */
    @Min(value = 0,"민들레 기부 개수는 0개 이상이어야 합니다.")
    private Integer dandelionCount;

    /**
     * 기부할 황금 민들레 개수
     * 음수 안됨
     */
    @Min(value = 0,"황금 민들레 기부 개수는 0개 이상이어야 합니다.")
    private Integer goldDandelionCount;

    /**
     * 총 기부 민들레 개수 계산
     * @return 일반 민들레와 황금 민들레의 합계
     * 기부 순위 계산, 유효성 검증 메서드때 사용
     */
    public int getTotalDonationCount(){
        int regularCount = (dandelionCount != null) ? dandelionCount : 0; //일반 민들레
        int goleCount = (goldDandelionCount != null) ? goldDandelionCount : 0; // 황금민들레
        return regularCount + goleCount;
    }

    /**
     * 유효한 기부 요청인지 검증
     * @return 최소 하나 이상의 민들레가 기부되는 경우 true
     */
    public boolean inValidDonation(){
        return getTotalDonationCount() > 0;
    }
}
