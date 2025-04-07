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
import com.ssafy.Dandelion.domain.dandelion.service.DandelionCollectionService;
import com.ssafy.Dandelion.domain.dandelion.service.DandelionDonationService;
import com.ssafy.Dandelion.domain.dandelion.service.DandelionRankingService;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.apiPayload.code.status.SuccessStatus;
import com.ssafy.Dandelion.global.auth.user.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/dandelions")
@RequiredArgsConstructor
@Slf4j
public class DandelionController {

	private final DandelionCollectionService collectionService;
	private final DandelionDonationService donationService;
	private final DandelionRankingService rankingService;

	@PostMapping("/gold-dandelion/generate")
	public ApiResponse<Void> triggerGoldDandelionGeneration() {
		try {
			collectionService.generateMonthlyGoldDandelions();
			return ApiResponse.onSuccess(null);
		} catch (Exception e) {
			throw e;
		}
	}

	// 일반 민들레 수집
	@PostMapping("/collections/personal/{dandelionId}")
	public ApiResponse<Void> collectPersonalDandelion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Integer dandelionId,
		@Valid @RequestBody DandelionLocationRequestDTO locationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		collectionService.collectDandelion(userId, dandelionId, locationRequestDTO);
		return ApiResponse.of(SuccessStatus.DANDELION_COLLECT_SUCCESS, null);
	}

	// 황금 민들레 수집
	@PostMapping("/collections/gold/{dandelionId}")
	public ApiResponse<Void> collectGoldDandelion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Integer dandelionId,
		@Valid @RequestBody DandelionLocationRequestDTO locationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		collectionService.collectGoldDandelion(userId, dandelionId, locationRequestDTO);
		return ApiResponse.of(SuccessStatus.GOLD_DANDELION_COLLECT_SUCCESS, null);
	}

	// 일반 민들레 위치 조회(10개)
	@PostMapping("/locations/personal")
	public ApiResponse<List<DandelionLocationResponseDTO>> getPersonalDandelions(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody DandelionLocationRequestDTO locationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		List<DandelionLocationResponseDTO> personalDandelions = collectionService.getPersonalDandelions(
			userId, locationRequestDTO);
		return ApiResponse.of(SuccessStatus.DANDELION_LOCATION_SUCCESS, personalDandelions);
	}

	// 황금 민들레 조회
	@GetMapping("/locations/gold")
	public ApiResponse<List<GoldDandelionLocationResponseDTO>> getUncollectedGoldDandelions() {
		List<GoldDandelionLocationResponseDTO> goldDandelions =
			collectionService.getUncollectedGoldDandelions();
		return ApiResponse.of(SuccessStatus.GOLD_DANDELION_LOCATION_SUCCESS, goldDandelions);
	}

	// 민들레 기부(일반 민들레, 황금 민들레)
	@PostMapping("/donations/organizations")
	public ApiResponse<Void> donateToOrganizations(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody DandelionDonationRequestDTO donationRequestDTO) {
		Integer userId = userDetails.getUserId().intValue();
		donationService.donateDandelions(userId, donationRequestDTO);
		return ApiResponse.of(SuccessStatus.DANDELION_DONATION_SUCCESS, null);
	}

	// 주간 기부 랭킹 조회
	@GetMapping("/donations/rankings/weekly")
	public ApiResponse<WeeklyRankingResponseDTO> getWeeklyRanking(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		WeeklyRankingResponseDTO weeklyRanking = rankingService.getWeeklyRanking(userId);
		return ApiResponse.onSuccess(weeklyRanking);
	}

	// 월간 황금 민들레 랭킹 조회
	@GetMapping("/donations/rankings/gold/monthly")
	public ApiResponse<MonthlyGoldRankingResponseDTO> getMonthlyGoldRanking(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Integer userId = userDetails.getUserId().intValue();
		MonthlyGoldRankingResponseDTO monthlyGoldRanking = rankingService.getMonthlyGoldRanking(userId);
		return ApiResponse.onSuccess(monthlyGoldRanking);
	}
}
