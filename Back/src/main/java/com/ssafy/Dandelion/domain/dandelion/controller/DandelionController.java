package com.ssafy.Dandelion.domain.dandelion.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;
import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;
import com.ssafy.Dandelion.domain.dandelion.service.DandelionService;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.auth.user.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/dandelions")
@RequiredArgsConstructor
@Slf4j
public class DandelionController {

	private final DandelionService dandelionService;

	/**
	 * 사용자 주변의 일반 민들레 위치 정보를 조회합니다.
	 * 클라이언트에서 POST 요청으로 현재 위치 정보를 전송하면 주변 민들레 정보를 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param locationRequestDTO 사용자의 현재 위치 정보
	 * @return 주변 민들레 위치 목록
	 */
	@PostMapping("/nearby")
	public ApiResponse<List<DandelionLocationResponseDTO>> getNearbyDandelions(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody DandelionLocationRequestDTO locationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		List<DandelionLocationResponseDTO> nearbyDandelions = dandelionService.getNearbyDandelions(userId,
			locationRequestDTO);
		return ApiResponse.onSuccess(nearbyDandelions);
	}

	/**
	 * 사용자 주변의 황금 민들레 위치 정보를 조회합니다.
	 * 클라이언트에서 POST 요청으로 현재 위치 정보를 전송하면 주변 황금 민들레 정보를 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param locationRequestDTO 사용자의 현재 위치 정보
	 * @return 주변 황금 민들레 위치 목록
	 */
	@PostMapping("/gold/nearby")
	public ApiResponse<List<GoldDandelionLocationResponseDTO>> getNearbyGoldDandelions(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody DandelionLocationRequestDTO locationRequestDTO) {
		List<GoldDandelionLocationResponseDTO> nearbyGoldDandelions = dandelionService.getNearbyGoldDandelions(
			locationRequestDTO);
		return ApiResponse.onSuccess(nearbyGoldDandelions);
	}

	/**
	 * 특정 일반 민들레를 수집합니다.
	 * 클라이언트에서 지정한 민들레 ID에 해당하는 민들레를 현재 로그인한 사용자의 소유로 설정합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param dandelionId 수집할 민들레 ID
	 * @return 수집 결과
	 */
	@PostMapping("/{dandelionId}/collect")
	public ApiResponse<Dandelion> collectDandelion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Integer dandelionId) {
		Integer userId = userDetails.getUserId().intValue();
		Dandelion collectedDandelion = dandelionService.collectDandelion(userId, dandelionId);
		return ApiResponse.onSuccess(collectedDandelion);
	}

	/**
	 * 특정 황금 민들레를 수집합니다.
	 * 클라이언트에서 지정한 황금 민들레 ID에 해당하는 민들레를 현재 로그인한 사용자의 소유로 설정합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param goldDandelionId 수집할 황금 민들레 ID
	 * @return 수집 결과
	 */
	@PostMapping("/gold/{goldDandelionId}/collect")
	public ApiResponse<GoldDandelion> collectGoldDandelion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Integer goldDandelionId) {
		Integer userId = userDetails.getUserId().intValue();
		GoldDandelion collectedGoldDandelion = dandelionService.collectGoldDandelion(userId, goldDandelionId);
		return ApiResponse.onSuccess(collectedGoldDandelion);
	}

	/**
	 * 민들레를 기부합니다.
	 * 클라이언트에서 전송한 기부 정보에 따라 현재 로그인한 사용자의 민들레를 기부 처리합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @param donationRequestDTO 기부 정보
	 * @return 기부 결과
	 */
	@PostMapping("/donate")
	public ApiResponse<Void> donateDandelions(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody DandelionDonationRequestDTO donationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		dandelionService.donateDandelions(userId, donationRequestDTO);
		return ApiResponse.onSuccess(null);
	}

	/**
	 * 사용자가 보유한 일반 민들레 개수를 조회합니다.
	 * 현재 로그인한 사용자가 보유한 일반 민들레의 총 개수를 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @return 보유한 일반 민들레 개수
	 */
	@GetMapping("/count")
	public ApiResponse<Integer> getUserDandelionCount(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		int count = dandelionService.getUserDandelionCount(userId);
		return ApiResponse.onSuccess(count);
	}

	/**
	 * 사용자가 보유한 황금 민들레 개수를 조회합니다.
	 * 현재 로그인한 사용자가 보유한 황금 민들레의 총 개수를 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @return 보유한 황금 민들레 개수
	 */
	@GetMapping("/gold/count")
	public ApiResponse<Integer> getUserGoldDandelionCount(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		int count = dandelionService.getUserGoldDandelionCount(userId);
		return ApiResponse.onSuccess(count);
	}

	/**
	 * 주간 기부 랭킹을 조회합니다.
	 * 최근 7일간의 민들레 기부 내역을 기준으로 순위를 집계하여 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @return 주간 기부 랭킹 정보
	 */
	@GetMapping("/donations/rankings/weekly")
	public ApiResponse<WeeklyRankingResponseDTO> getWeeklyRanking(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		WeeklyRankingResponseDTO weeklyRanking = dandelionService.getWeeklyRanking(userId);
		return ApiResponse.onSuccess(weeklyRanking);
	}

	/**
	 * 월간 황금 민들레 랭킹을 조회합니다.
	 * 이번 달의 황금 민들레 기부 내역을 기준으로 순위를 집계하여 반환합니다.
	 *
	 * @param userDetails 인증된 사용자 정보
	 * @return 월간 황금 민들레 랭킹 정보
	 */
	@GetMapping("/donations/rankings/gold/monthly")
	public ApiResponse<MonthlyGoldRankingResponseDTO> getMonthlyGoldRanking(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		MonthlyGoldRankingResponseDTO monthlyGoldRanking = dandelionService.getMonthlyGoldRanking(userId);
		return ApiResponse.onSuccess(monthlyGoldRanking);
	}
}
