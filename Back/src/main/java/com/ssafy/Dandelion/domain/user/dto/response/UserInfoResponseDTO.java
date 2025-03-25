package com.ssafy.Dandelion.domain.user.dto.response;

import com.ssafy.Dandelion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDTO {
    private Integer userId;
    private String name;
    private String email;
    private String userKey;
    private int dandelionCount;
    private int dandelionUseCount;
    private int goldDandelionCount;
    private int goldDandelionUseCount;
    private String createdAt;
    private String updatedAt;

    // User 엔터티로부터 DTO 생성을 위한 정적 팩토리 메서드
    public static UserInfoResponseDTO fromEntity(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return UserInfoResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .userKey(user.getUserKey())
                .dandelionCount(user.getDandelionCount())
                .dandelionUseCount(user.getDandelionUseCount())
                .goldDandelionCount(user.getGoldDandelionCount())
                .goldDandelionUseCount(user.getGoldDandelionUseCount())
                .createdAt(user.getCreatedAt().format(formatter))
                .updatedAt(user.getUpdatedAt().format(formatter))
                .build();
    }
}
