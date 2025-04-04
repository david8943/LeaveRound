package com.ssafy.Dandelion.domain.user.dto.response;

import java.time.format.DateTimeFormatter;

import com.ssafy.Dandelion.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private int goldDandelionCount;
	private int totalDonationCount;
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
			.goldDandelionCount(user.getGoldDandelionCount())
			.totalDonationCount(user.getDandelionUseCount() + (user.getGoldDandelionUseCount() * 100))
			.createdAt(user.getCreatedAt().format(formatter))
			.updatedAt(user.getUpdatedAt().format(formatter))
			.build();
	}
}
