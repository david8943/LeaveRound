package com.ssafy.Dandelion.domain.dandelion.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LocationRequestDTO {

    /**
     * 위도(-90도 ~ 90도)
     */
    @NotNull(message = "위도 정보는 필수입니다.")
    @DecimalMin(value = "-90.0", message = "위도는 -90도보다 작을 수 없습니다.")
    @DecimalMax(value = "90.0", message = "위도는 90도보다 클 수 없습니다.")
    private BigDecimal myLatitude;

    /**
     * 경도 (-180° ~ 180°)
     */
    @NotNull(message = "경도 정보는 필수입니다.")
    @DecimalMin(value = "-180.0", message = "경도는 -180도보다 작을 수 없습니다.")
    @DecimalMax(value = "180.0", message = "경도는 180도보다 클 수 없습니다.")
    private BigDecimal myLongitude;

    /**
     * 현재 위치가 유효한지 검증
     *
     * @return 위치 유효 여부
     */
    public boolean isValidLocation() {
        return myLatitude != null && myLongitude != null;
    }

    /**
     * 위도 값 설정
     *
     * @param latitude 새 위도 값
     * @return 현재 객체
     */
    public LocationRequestDTO updateLatitude(BigDecimal latitude) {
        this.myLatitude = latitude;
        return this;
    }

    /**
     * 경도 값 설정
     *
     * @param longitude 새 위도 값
     * @return 현재 객체
     */
    public LocationRequestDTO updateLongitude(BigDecimal longitude) {
        this.myLongitude = longitude;
        return this;
    }
}
