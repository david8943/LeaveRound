package com.ssafy.Dandelion.domain.dandelion.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MyDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MyGoldDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;
import com.ssafy.Dandelion.domain.dandelion.entity.DandelionDonationInfo;
import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;
import com.ssafy.Dandelion.domain.dandelion.entity.constant.DandelionValue;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionDonationInfoRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionLocationRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.GoldDandelionRepository;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.DandelionHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DandelionServiceImpl implements DandelionService {

	private final DandelionRepository dandelionRepository;
	private final GoldDandelionRepository goldDandelionRepository;
	private final DandelionLocationRepository dandelionLocationRepository;
	private final DandelionDonationInfoRepository donationInfoRepository;
	private final UserRepository userRepository;

	/**
	 * 사용자가 일반 민들레를 수집합니다.
	 * 이미 수집된 민들레는 예외를 발생시킵니다.
	 *
	 * @param userId 수집하는 사용자 ID
	 * @param dandelionId 수집할 민들레 ID
	 * @return 수집된 민들레 엔티티
	 * @throws NotFoundHandler 민들레가 존재하지 않는 경우
	 * @throws DandelionHandler 이미 수집된 민들레인 경우, 수집 거리가 너무 먼 경우
	 */
	@Override
	@Transactional
	public Dandelion collectDandelion(Integer userId, Integer dandelionId) {
		// 해당 사용자와 민들레 ID가 유효한지 확인
		Dandelion dandelion = dandelionRepository.findById(dandelionId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.DANDELION_NOT_FOUND));

		// 이미 수집된 민들레인지 확인
		if (dandelion.getUserId() != null) {
			throw new DandelionHandler(ErrorStatus.DANDELION_ALREADY_COLLECT);
		}

		// TODO: 민들레 수집 거리 체크 추가
		// 이 부분은 실제 사용자 위치와 민들레 위치를 계산하여 거리가 일정 범위 내인지 확인해야 함
		// if (거리가 너무 멀면) {
		//     throw new DandelionHandler(ErrorStatus.DANDELION_TOO_FAR_TO_COLLECT);
		// }

		// 민들레 수집 처리 (사용자 ID 설정)
		dandelion.setUserId(userId);

		// Redis에서 해당 민들레 위치 정보 삭제
		dandelionLocationRepository.removeDandelion(userId, dandelionId);

		// 업데이트된 민들레 정보 저장
		return dandelionRepository.save(dandelion);
	}

	/**
	 * 사용자가 황금 민들레를 수집합니다.
	 * 이미 수집된 황금 민들레는 예외를 발생시킵니다.
	 *
	 * @param userId 수집하는 사용자 ID
	 * @param goldDandelionId 수집할 황금 민들레 ID
	 * @return 수집된 황금 민들레 엔티티
	 * @throws NotFoundHandler 황금 민들레가 존재하지 않는 경우
	 * @throws DandelionHandler 이미 수집된 황금 민들레인 경우, 수집 거리가 너무 먼 경우
	 */
	@Override
	@Transactional
	public GoldDandelion collectGoldDandelion(Integer userId, Integer goldDandelionId) {
		// 해당 황금 민들레 ID가 유효한지 확인
		GoldDandelion goldDandelion = goldDandelionRepository.findById(goldDandelionId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.GOLD_DANDELION_NOT_FOUND));

		// 이미 수집된 황금 민들레인지 확인
		if (goldDandelion.getUserId() != null) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDLION_ALREADY_COLLECT);
		}

		// TODO: 황금 민들레 수집 거리 체크 추가
		// 이 부분은 실제 사용자 위치와 황금 민들레 위치를 계산하여 거리가 일정 범위 내인지 확인해야 함
		// if (거리가 너무 멀면) {
		//     throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_TOO_FAR_TO_COLLECT);
		// }

		// 황금 민들레 수집 처리 (사용자 ID 설정 및 수집 시간 기록)
		goldDandelion.setUserId(userId);
		goldDandelion.setAcquiredAt(LocalDateTime.now());

		// Redis에서 해당 황금 민들레 위치 정보 삭제
		dandelionLocationRepository.removeGoldDandelion(goldDandelionId);

		// 업데이트된 황금 민들레 정보 저장
		return goldDandelionRepository.save(goldDandelion);
	}

	/**
	 * 사용자 주변의 일반 민들레 위치 정보를 조회합니다.
	 * 100m 반경 내의 민들레를 검색합니다.
	 *
	 * @param userId 조회하는 사용자 ID
	 * @param locationRequestDTO 사용자의 현재 위치 정보
	 * @return 주변 민들레 위치 목록
	 * @throws DandelionHandler 위치 정보가 유효하지 않은 경우
	 */
	@Override
	public List<DandelionLocationResponseDTO> getNearbyDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO) {
		// 위치 정보가 유효한지 확인
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		// 현재 사용자 위치 주변의 민들레 검색
		var nearbyDandelions = dandelionLocationRepository.findDandelionsNearby(
			userId,
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude()
		);

		// 결과가 없는 경우 빈 리스트 반환
		if (nearbyDandelions == null) {
			return new ArrayList<>();
		}

		// 검색 결과를 DTO로 변환
		return nearbyDandelions.getContent().stream()
			.map(geoLocation -> {
				Integer dandelionId = (Integer)geoLocation.getContent().getName();
				var position = geoLocation.getContent().getPoint();

				return DandelionLocationResponseDTO.builder()
					.dandelionId(dandelionId)
					.latitude(BigDecimal.valueOf(position.getY()))
					.longitude(BigDecimal.valueOf(position.getX()))
					.build();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 사용자 주변의 황금 민들레 위치 정보를 조회합니다.
	 * 100m 반경 내의 황금 민들레를 검색합니다.
	 *
	 * @param locationRequestDTO 사용자의 현재 위치 정보
	 * @return 주변 황금 민들레 위치 목록
	 * @throws DandelionHandler 위치 정보가 유효하지 않은 경우
	 */
	@Override
	public List<GoldDandelionLocationResponseDTO> getNearbyGoldDandelions(
		DandelionLocationRequestDTO locationRequestDTO) {
		// 위치 정보가 유효한지 확인
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		// 현재 위치 주변의 황금 민들레 검색
		var nearbyGoldDandelions = dandelionLocationRepository.findGoldDandelionsNearby(
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude()
		);

		// 결과가 없는 경우 빈 리스트 반환
		if (nearbyGoldDandelions == null) {
			return new ArrayList<>();
		}

		// 검색 결과를 DTO로 변환
		return nearbyGoldDandelions.getContent().stream()
			.map(geoLocation -> {
				var position = geoLocation.getContent().getPoint();

				return GoldDandelionLocationResponseDTO.builder()
					.latitude(BigDecimal.valueOf(position.getY()))
					.longitude(BigDecimal.valueOf(position.getX()))
					.build();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 사용자가 민들레를 기부합니다.
	 * 일반 민들레와 황금 민들레를 특정 프로젝트에 기부할 수 있습니다.
	 *
	 * @param userId 기부하는 사용자 ID
	 * @param donationRequestDTO 기부 정보 (일반 민들레 개수, 황금 민들레 개수, 기부처 ID)
	 * @throws DandelionHandler 기부할 민들레가 없거나, 보유량보다 많이 기부하려는 경우, 기부처가 존재하지 않는 경우
	 */
	@Override
	@Transactional
	public void donateDandelions(Integer userId, DandelionDonationRequestDTO donationRequestDTO) {
		// 기부할 민들레 개수 확인
		int normalCount = donationRequestDTO.getDandelionCount() != null ? donationRequestDTO.getDandelionCount() : 0;
		int goldCount =
			donationRequestDTO.getGoldDandelionCount() != null ? donationRequestDTO.getGoldDandelionCount() : 0;

		// 기부할 민들레가 없는 경우 예외 처리
		if (normalCount == 0 && goldCount == 0) {
			throw new DandelionHandler(ErrorStatus.EMPTY_DONATION);
		}

		// 사용자가 가진 민들레 개수 확인
		int userNormalCount = dandelionRepository.countByUserId(userId);
		int userGoldCount = goldDandelionRepository.countByUserId(userId);

		// 사용자가 가진 민들레보다 많은 개수를 기부하려는 경우 예외 처리
		if (normalCount > 0 && normalCount > userNormalCount) {
			throw new DandelionHandler(ErrorStatus.DANDELION_OVER_COUNT);
		}

		if (goldCount > 0 && goldCount > userGoldCount) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_OVER_COUNT);
		}

		// 기부처 ID가 유효한지 확인 (0이면 랜덤 기부)
		if (donationRequestDTO.getProjectId() != null && donationRequestDTO.getProjectId() != 0) {
			// TODO: 기부처 존재 여부 확인
			// organizationProjectRepository.findById(donationRequestDTO.getProjectId())
			//    .orElseThrow(() -> new DandelionHandler(ErrorStatus.ORGANIZATION_EMPTY));
		}

		// 기부 정보 생성 및 저장
		DandelionDonationInfo donationInfo = DandelionDonationInfo.builder()
			.userId(userId)
			.organizationProjectId(donationRequestDTO.getProjectId() != null ? donationRequestDTO.getProjectId() : 0)
			.useDandelionCount(normalCount)
			.useGoldDandelionCount(goldCount)
			.build();

		donationInfoRepository.save(donationInfo);

		// TODO: 민들레 기부 후 처리 (실제 민들레 삭제 로직이 필요할 수 있음)
	}

	/**
	 * 사용자가 보유한 일반 민들레 개수를 조회합니다.
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 보유한 일반 민들레 개수
	 */
	@Override
	public int getUserDandelionCount(Integer userId) {
		return dandelionRepository.countByUserId(userId);
	}

	/**
	 * 사용자가 보유한 황금 민들레 개수를 조회합니다.
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 보유한 황금 민들레 개수
	 */
	@Override
	public int getUserGoldDandelionCount(Integer userId) {
		return goldDandelionRepository.countByUserId(userId);
	}

	/**
	 * 주간 기부 랭킹을 조회합니다.
	 * 최근 7일간의 민들레 기부 내역을 기준으로 순위를 집계합니다.
	 * 황금 민들레는 일반 민들레 100개로 환산됩니다.
	 *
	 * @param userId 현재 사용자 ID
	 * @return 주간 기부 랭킹 정보와 사용자의 랭킹 정보
	 */
	@Override
	public WeeklyRankingResponseDTO getWeeklyRanking(Integer userId) {
		// 일주일 기간 설정 (현재 시점에서 7일 전까지)
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusDays(7);

		// 기간 내 기부 데이터 조회
		List<Object[]> donationRankingData = donationInfoRepository.findDonationsByTypeInPeriod(startDate, endDate);

		List<DandelionRankDTO> rankInfos = new ArrayList<>();
		MyDandelionRankDTO myRankInfo = null;
		int myRank = 0;

		// 랭킹 정보 생성
		for (int i = 0; i < donationRankingData.size(); i++) {
			Object[] data = donationRankingData.get(i);
			Integer donorId = (Integer)data[0];
			Long normalDonation = ((Number)data[1]).longValue();
			Long goldDonation = ((Number)data[2]).longValue();

			// 황금 민들레 1개 = 일반 민들레 100개로 계산 (DandelionValue 적용)
			Long totalDonation = normalDonation + (goldDonation * DandelionValue.GOLD.getValue());

			// 사용자 정보 조회
			String userName = userRepository.findById(donorId)
				.map(user -> user.getName())
				.orElse("Unknown User");

			DandelionRankDTO rankDTO = DandelionRankDTO.builder()
				.rank(i + 1)
				.name(userName)
				.donateCount(totalDonation.intValue())
				.build();

			rankInfos.add(rankDTO);

			// 현재 사용자의 랭킹 정보 저장
			if (donorId.equals(userId)) {
				myRank = i + 1;
				myRankInfo = MyDandelionRankDTO.builder()
					.myRank(myRank)
					.myName(userName)
					.myDonateCount(totalDonation.intValue())
					.build();
			}
		}

		// 현재 사용자의 랭킹 정보가 없는 경우 기본값 설정
		if (myRankInfo == null) {
			myRankInfo = MyDandelionRankDTO.builder()
				.myRank(0)
				.myName(userRepository.findById(userId)
					.map(user -> user.getName())
					.orElse("Unknown User"))
				.myDonateCount(0)
				.build();
		}

		return WeeklyRankingResponseDTO.builder()
			.rankInfos(rankInfos)
			.myRankInfo(myRankInfo)
			.build();
	}

	/**
	 * 월간 황금 민들레 수집 랭킹을 조회합니다.
	 * 이번 달에 사용자들이 수집한 황금 민들레의 총 개수(기부한 것도 포함)를 기준으로 순위를 집계합니다.
	 *
	 * @param userId 현재 사용자 ID
	 * @return 월간 황금 민들레 수집 랭킹 정보와 사용자의 랭킹 정보
	 */
	@Override
	public MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId) {
		// 현재 달의 시작일과 마지막일 설정
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		// 기간 내 황금 민들레 수집 데이터 조회 (현재 보유 + 기부한 것 포함)
		// 현재 보유한 황금 민들레: userKey -> goldDandelionCount
		// 이번 달에 기부한 황금 민들레: useGoldDandelionCount

		// TODO: 이번 달에 사용자가 수집한 황금 민들레 총 개수를 조회하는 메서드 필요
		// 현재는 기부 데이터만 사용하여 랭킹 생성
		List<Object[]> goldDonationRankingData = donationInfoRepository.findMonthlyGoldDonationRanking(startDate,
			endDate);

		// 사용자별 황금 민들레 수집 맵 생성
		Map<Integer, Integer> userGoldCountMap = new HashMap<>();

		// 기부 데이터 합산
		for (Object[] data : goldDonationRankingData) {
			Integer donorId = (Integer)data[0];
			Long goldDonationCount = ((Number)data[1]).longValue();
			userGoldCountMap.put(donorId, goldDonationCount.intValue());
		}

		// 현재 보유 개수도 추가 (이 부분은 실제 구현 필요)
		// 사용자별 황금 민들레 현재 보유 개수 조회 로직 필요

		// 사용자별 수집 정보를 내림차순으로 정렬
		List<Map.Entry<Integer, Integer>> sortedGoldCounts = userGoldCountMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
			.collect(Collectors.toList());

		List<GoldDandelionRankDTO> goldInfos = new ArrayList<>();
		MyGoldDandelionRankDTO myGoldInfo = null;
		int myRank = 0;

		// 랭킹 정보 생성
		for (int i = 0; i < sortedGoldCounts.size(); i++) {
			Map.Entry<Integer, Integer> entry = sortedGoldCounts.get(i);
			Integer userID = entry.getKey();
			Integer goldCount = entry.getValue();

			// 사용자 정보 조회
			String userName = userRepository.findById(userID)
				.map(user -> user.getName())
				.orElse("Unknown User");

			GoldDandelionRankDTO rankDTO = GoldDandelionRankDTO.builder()
				.name(userName)
				.goldCount(goldCount)
				.build();

			goldInfos.add(rankDTO);

			// 현재 사용자의 랭킹 정보 저장
			if (userID.equals(userId)) {
				myRank = i + 1;
				myGoldInfo = MyGoldDandelionRankDTO.builder()
					.myName(userName)
					.myGoldCount(goldCount)
					.build();
			}
		}

		// 현재 사용자의 랭킹 정보가 없는 경우 기본값 설정
		if (myGoldInfo == null) {
			myGoldInfo = MyGoldDandelionRankDTO.builder()
				.myName(userRepository.findById(userId)
					.map(user -> user.getName())
					.orElse("Unknown User"))
				.myGoldCount(0)
				.build();
		}

		return MonthlyGoldRankingResponseDTO.builder()
			.goldInfos(goldInfos)
			.myGoldInfo(myGoldInfo)
			.build();
	}
}
