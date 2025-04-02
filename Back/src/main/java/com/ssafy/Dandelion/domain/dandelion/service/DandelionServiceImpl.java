package com.ssafy.Dandelion.domain.dandelion.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.geo.Point;
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
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionDonationInfoRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionLocationRedisRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.GoldDandelionRepository;
import com.ssafy.Dandelion.domain.organization.repository.OrganizationProjectRepository;
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
	private final DandelionLocationRedisRepository dandelionLocationRedisRepository;
	private final DandelionDonationInfoRepository donationInfoRepository;
	private final OrganizationProjectRepository organizationProjectRepository;
	private final UserRepository userRepository;

	// 민들레 거리 제한 상수 (미터 단위)
	private static final double MAX_COLLECTION_DISTANCE = 100.0;
	// 사용자당 제공할 민들레 개수
	private static final int TARGET_DANDELION_COUNT = 10;
	// 월별 생성할 황금 민들레 개수
	private static final int MONTHLY_GOLD_DANDELIONS = 5;
	// 주간 랭킹 표시 개수
	private static final int WEEKLY_RANKING_LIMIT = 10;

	// 민들레 기부
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

		// 기부 가능한 민들레 개수 확인
		int availableNormal = getAvailableDandelionCount(userId);
		int availableGold = getAvailableGoldDandelionCount(userId);

		// 사용자가 가진 민들레보다 많은 개수를 기부하려는 경우 예외 처리
		if (normalCount > 0 && normalCount > availableNormal) {
			throw new DandelionHandler(ErrorStatus.DANDELION_OVER_COUNT);
		}

		if (goldCount > 0 && goldCount > availableGold) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_OVER_COUNT);
		}

		// 기부처 ID 처리
		Integer projectId = donationRequestDTO.getProjectId();
		if (projectId == null || projectId == 0) {
			// 기본 기부처 설정 (시스템 기본값)
			projectId = 1;
		}

		// 기부 정보 생성 및 저장
		DandelionDonationInfo donationInfo = DandelionDonationInfo.builder()
			.userId(userId)
			.organizationProjectId(projectId)
			.useDandelionCount(normalCount)
			.useGoldDandelionCount(goldCount)
			.build();

		donationInfoRepository.save(donationInfo);

		log.info("{} 사용자가 {} 일반 민들레랑 {} 황금 민들레를 {} 프로젝트에 기부함",
			userId, normalCount, goldCount, projectId);
	}

	// 보유한 일반 민들레 개수 조회
	@Override
	public int getTotalDandelionCount(Integer userId) {
		return dandelionRepository.countByUserId(userId);
	}

	// 보유한 황금 민들레 총 개수 조회
	@Override
	public int getTotalGoldDandelionCount(Integer userId) {
		return goldDandelionRepository.countByUserId(userId);
	}

	// 기부 가능한 일반 민들레 개수
	@Override
	public int getAvailableDandelionCount(Integer userId) {
		int totalCollected = getTotalDandelionCount(userId);
		Integer totalDonated = donationInfoRepository.sumUseDandelionCountByUserId(userId);
		return totalCollected - (totalDonated != null ? totalDonated : 0);
	}

	// 기부 가능한 황금 민들레 개수
	@Override
	public int getAvailableGoldDandelionCount(Integer userId) {
		int totalCollected = getTotalGoldDandelionCount(userId);
		Integer totalDonated = donationInfoRepository.sumUseGoldDandelionCountByUserId(userId);
		return totalCollected - (totalDonated != null ? totalDonated : 0);
	}

	// 주간 기부 랭킹 조회
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

		// 랭킹 정보 생성 및 모든 사용자의 랭킹 계산
		Map<Integer, Integer> userTotalDonationMap = new HashMap<>();

		for (Object[] data : donationRankingData) {
			Integer donorId = (Integer)data[0];
			Long normalDonation = ((Number)data[1]).longValue();

			// 사용자별 총 기부 개수 합산
			userTotalDonationMap.merge(donorId, normalDonation.intValue(), Integer::sum);
		}

		// 기부 개수 내림차순으로 정렬
		List<Map.Entry<Integer, Integer>> sortedDonations = userTotalDonationMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
			.toList();

		// 상위 10명만 랭킹에 포함
		int limit = Math.min(WEEKLY_RANKING_LIMIT, sortedDonations.size());
		for (int i = 0; i < limit; i++) {
			Map.Entry<Integer, Integer> entry = sortedDonations.get(i);
			Integer donorId = entry.getKey();
			Integer totalDonation = entry.getValue();

			// 사용자 정보 조회
			String userName = userRepository.findById(donorId)
				.map(user -> user.getName())
				.orElse("Unknown User");

			DandelionRankDTO rankDTO = DandelionRankDTO.builder()
				.rank(i + 1)
				.name(userName)
				.donateCount(totalDonation)
				.build();

			rankInfos.add(rankDTO);

			// 현재 사용자의 랭킹 정보 저장
			if (donorId.equals(userId)) {
				myRank = i + 1;
				myRankInfo = MyDandelionRankDTO.builder()
					.myRank(myRank)
					.myName(userName)
					.myDonateCount(totalDonation)
					.build();
			}
		}

		// 상위 10명에 현재 사용자가 없는 경우 추가 확인
		if (myRankInfo == null) {
			// 전체 랭킹에서 현재 사용자 찾기
			for (int i = limit; i < sortedDonations.size(); i++) {
				Map.Entry<Integer, Integer> entry = sortedDonations.get(i);
				Integer donorId = entry.getKey();

				if (donorId.equals(userId)) {
					Integer totalDonation = entry.getValue();
					String userName = userRepository.findById(donorId)
						.map(user -> user.getName())
						.orElse("Unknown User");

					myRankInfo = MyDandelionRankDTO.builder()
						.myRank(i + 1)
						.myName(userName)
						.myDonateCount(totalDonation)
						.build();
					break;
				}
			}

			// 랭킹에 사용자가 없는 경우 기본값 설정
			if (myRankInfo == null) {
				myRankInfo = MyDandelionRankDTO.builder()
					.myRank(0)
					.myName(userRepository.findById(userId)
						.map(user -> user.getName())
						.orElse("Unknown User"))
					.myDonateCount(0)
					.build();
			}
		}

		return WeeklyRankingResponseDTO.builder()
			.rankInfos(rankInfos)
			.myRankInfo(myRankInfo)
			.build();
	}

	// 월간 황금 민들레 순위
	@Override
	public MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId) {
		// 현재 달의 시작일과 마지막일 설정
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		// 1. 이번 달에 사용자가 수집한 황금 민들레 데이터 조회
		List<Object[]> goldCollectionData = goldDandelionRepository.findGoldDandelionCollectionRankingBetween(startDate,
			endDate);

		// 2. 이번 달에 사용자가 기부한 황금 민들레 데이터 조회
		List<Object[]> goldDonationData = donationInfoRepository.findMonthlyGoldDonationRanking(startDate, endDate);

		// 사용자별 황금 민들레 합산 맵 생성 (수집 + 기부)
		Map<Integer, Integer> userGoldCountMap = new HashMap<>();

		// 수집 데이터 합산
		for (Object[] data : goldCollectionData) {
			Integer userID = (Integer)data[0];
			Long collectionCount = ((Number)data[1]).longValue();
			userGoldCountMap.put(userID, collectionCount.intValue());
		}

		// 기부 데이터 합산 (이미 기부한 것도 수집한 것으로 간주)
		for (Object[] data : goldDonationData) {
			Integer userID = (Integer)data[0];
			Long donationCount = ((Number)data[1]).longValue();

			// 이미 해당 사용자의 데이터가 있으면 합산, 없으면 새로 추가
			userGoldCountMap.merge(userID, donationCount.intValue(), Integer::sum);
		}

		// 합산 데이터를 내림차순으로 정렬
		List<Map.Entry<Integer, Integer>> sortedGoldCounts = userGoldCountMap.entrySet().stream()
			.sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
			.collect(Collectors.toList());

		List<GoldDandelionRankDTO> goldInfos = new ArrayList<>();
		MyGoldDandelionRankDTO myGoldInfo = null;

		// 랭킹 정보 생성
		for (int i = 0; i < sortedGoldCounts.size(); i++) {
			Map.Entry<Integer, Integer> entry = sortedGoldCounts.get(i);
			Integer userID = entry.getKey();
			Integer goldCount = entry.getValue();

			if (goldCount <= 0) {
				continue;
			}

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

	// 사용자 주변 민들레 조회 및 재배치
	@Override
	@Transactional
	public List<DandelionLocationResponseDTO> getAndRelocatePersonalDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO) {
		// 위치 정보가 유효한지 확인
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		// 주변 민들레 조회
		List<DandelionLocationResponseDTO> nearbyDandelions = getNearbyDandelions(userId, locationRequestDTO);

		// 민들레가 없거나 10개 미만인 경우 새로 생성
		if (nearbyDandelions.isEmpty()) {
			// 처음 사용자라면 10개 민들레 생성
			createInitialDandelions(userId, locationRequestDTO);

			// 생성 후 다시 조회
			return getNearbyDandelions(userId, locationRequestDTO);
		} else if (nearbyDandelions.size() < TARGET_DANDELION_COUNT) {
			// 부족한 민들레 개수
			int needToRelocateCount = TARGET_DANDELION_COUNT - nearbyDandelions.size();

			// 재배치할 민들레 선택 및 재배치
			relocateFarDandelions(userId, locationRequestDTO, needToRelocateCount);

			// 재배치 후 다시 조회
			return getNearbyDandelions(userId, locationRequestDTO);
		}

		// 이미 충분한 민들레가 있으면 그냥 반환
		return nearbyDandelions;
	}

	// 미수집 된 황금 민들레 조회
	@Override
	public List<GoldDandelionLocationResponseDTO> getMonthlyUncollectedGoldDandelions() {
		// 이번 달의 시작일과 마지막일 설정
		YearMonth currentMonth = YearMonth.now();
		LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(23, 59, 59);

		// 이번 달 미수집 황금 민들레 조회
		List<GoldDandelion> uncollectedGoldDandelions =
			goldDandelionRepository.findUncollectedGoldDandelionsBetween(startDate, endDate);

		// DTO로 변환하여 반환
		return uncollectedGoldDandelions.stream()
			.map(dandelion -> GoldDandelionLocationResponseDTO.builder()
				.goldDandelionId(dandelion.getGoldDandelionId())
				.latitude(dandelion.getLatitude())
				.longitude(dandelion.getLongitude())
				.build())
			.collect(Collectors.toList());
	}

	// 일반 민들레 수집, 사용자와 민들레 간의 거리를 확인해서 100 미터 이내인 경우에만 수집 가능
	@Override
	@Transactional
	public void collectDandelionWithDistanceCheck(Integer userId, Integer dandelionId,
		DandelionLocationRequestDTO locationRequestDTO) {
		// 해당 사용자와 민들레 ID가 유효한지 확인
		Dandelion dandelion = dandelionRepository.findById(dandelionId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.DANDELION_NOT_FOUND));

		// 이미 수집된 민들레인지 확인
		if (dandelion.getUserId() != null) {
			throw new DandelionHandler(ErrorStatus.DANDELION_ALREADY_COLLECT);
		}

		// 거리 계산
		double distance = calculateDistance(
			locationRequestDTO.getMyLatitude().doubleValue(),
			locationRequestDTO.getMyLongitude().doubleValue(),
			dandelion.getLatitude().doubleValue(),
			dandelion.getLongitude().doubleValue()
		);

		// 거리가 너무 멀면 예외 발생
		if (distance > MAX_COLLECTION_DISTANCE) {
			throw new DandelionHandler(ErrorStatus.DANDELION_TOO_FAR_TO_COLLECT);
		}

		// 민들레 수집 처리 (사용자 ID 설정)
		dandelion.setUserId(userId);

		// Redis에서 해당 민들레 위치 정보 삭제
		dandelionLocationRedisRepository.removeDandelion(userId, dandelionId);

		// 업데이트된 민들레 정보 저장
		dandelionRepository.save(dandelion);
	}

	// 황금 민들레 수집, 사용자와 황금 민들레 위치 거리가 100미터 이내여야 성공
	@Override
	@Transactional
	public void collectGoldDandelionWithDistanceCheck(Integer userId, Integer goldDandelionId,
		DandelionLocationRequestDTO locationRequestDTO) {
		// 해당 황금 민들레 ID가 유효한지 확인
		GoldDandelion goldDandelion = goldDandelionRepository.findById(goldDandelionId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.GOLD_DANDELION_NOT_FOUND));

		// 이미 수집된 황금 민들레인지 확인
		if (goldDandelion.getUserId() != null) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_ALREADY_COLLECT);
		}

		// 거리 계산
		double distance = calculateDistance(
			locationRequestDTO.getMyLatitude().doubleValue(),
			locationRequestDTO.getMyLongitude().doubleValue(),
			goldDandelion.getLatitude().doubleValue(),
			goldDandelion.getLongitude().doubleValue()
		);

		// 거리가 너무 멀면 예외 발생
		if (distance > MAX_COLLECTION_DISTANCE) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_TOO_FAR_TO_COLLECT);
		}

		// 황금 민들레 수집 처리 (사용자 ID 설정 및 수집 시간 기록)
		goldDandelion.setUserId(userId);
		goldDandelion.setAcquiredAt(LocalDateTime.now());

		// Redis에서 해당 황금 민들레 위치 정보 삭제
		dandelionLocationRedisRepository.removeGoldDandelion(goldDandelionId);

		// 업데이트된 황금 민들레 정보 저장
		goldDandelionRepository.save(goldDandelion);
	}

	// 월별 황금 민들레 생성 및 관리(스케줄러에 의해 매월 초에 실행됨)
	@Override
	@Transactional
	public void generateMonthlyGoldDandelions() {
		// 이전 달의 시작일과 마지막일 설정
		YearMonth lastMonth = YearMonth.now().minusMonths(1);
		LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);

		// 이전 달의 미수집 황금 민들레 조회 및 삭제
		List<GoldDandelion> previousGoldDandelions =
			goldDandelionRepository.findUncollectedGoldDandelionsBetween(startOfLastMonth, endOfLastMonth);

		log.info("{} 지난달에 수집 안된 황금 민들레 삭제", previousGoldDandelions.size());

		// Redis에서 해당 황금 민들레 위치 정보 삭제 후 DB에서도 삭제
		for (GoldDandelion goldDandelion : previousGoldDandelions) {
			dandelionLocationRedisRepository.removeGoldDandelion(goldDandelion.getGoldDandelionId());
			goldDandelionRepository.delete(goldDandelion);
		}

		// 새로운 황금 민들레 5개 생성
		log.info("{}개 이번 달 새로운 황금 민들레 생성", MONTHLY_GOLD_DANDELIONS);

		Map<Integer, Point> newGoldDandelions = new HashMap<>();
		Random random = new Random();

		// 전국 범위 내 랜덤 위치 생성 (한국 대략적 범위)
		double minLat = 33.0; // 제주도 남단
		double maxLat = 38.5; // 최북단
		double minLng = 125.0; // 서해안
		double maxLng = 131.0; // 동해안

		for (int i = 0; i < MONTHLY_GOLD_DANDELIONS; i++) {
			// 랜덤 위치 생성
			double lat = minLat + (maxLat - minLat) * random.nextDouble();
			double lng = minLng + (maxLng - minLng) * random.nextDouble();

			// 새 황금 민들레 엔티티 생성 및 저장
			GoldDandelion goldDandelion = GoldDandelion.builder()
				.userId(null) // 아직 수집되지 않음
				.latitude(BigDecimal.valueOf(lat))
				.longitude(BigDecimal.valueOf(lng))
				.acquiredAt(null) // 아직 수집되지 않음
				.build();

			goldDandelion = goldDandelionRepository.save(goldDandelion);

			// Redis에 저장하기 위한 맵에 추가
			newGoldDandelions.put(goldDandelion.getGoldDandelionId(), new Point(lng, lat));
		}

		// Redis에 저장
		dandelionLocationRedisRepository.saveGoldDandelionLocations(newGoldDandelions);

		log.info("새로운 월별 황금 민들레 생성 성공");
	}

	// 내부 유틸리티 메소드들

	// 사용자 주변 민들레 정보 조회
	private List<DandelionLocationResponseDTO> getNearbyDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO) {
		log.debug("Finding nearby dandelions for user: {}", userId);
		log.debug("Latitude: {}, Longitude: {}",
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude());

		var nearbyDandelions = dandelionLocationRedisRepository.findDandelionsNearby(
			userId,
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude()
		);

		log.debug("Nearby dandelions result: {}", nearbyDandelions);

		// 결과가 없거나 빈 경우
		if (nearbyDandelions == null ||
			nearbyDandelions.getContent() == null ||
			nearbyDandelions.getContent().isEmpty()) {

			log.debug("No nearby dandelions found. Creating initial dandelions.");
			createInitialDandelions(userId, locationRequestDTO);

			// 다시 조회
			nearbyDandelions = dandelionLocationRedisRepository.findDandelionsNearby(
				userId,
				locationRequestDTO.getMyLatitude(),
				locationRequestDTO.getMyLongitude()
			);

			log.debug("After creating initial dandelions, result: {}", nearbyDandelions);
		}

		// 결과가 여전히 없다면 빈 리스트 반환
		if (nearbyDandelions == null ||
			nearbyDandelions.getContent() == null ||
			nearbyDandelions.getContent().isEmpty()) {
			log.warn("Still no dandelions found after creation attempt.");
			return new ArrayList<>();
		}

		// 검색 결과를 DTO로 변환
		return nearbyDandelions.getContent().stream()
			.map(geoLocation -> {
				Integer dandelionId = (Integer)geoLocation.getContent().getName();

				return DandelionLocationResponseDTO.builder()
					.dandelionId(dandelionId)
					// point가 null일 경우 사용자 위치 기반으로 랜덤 생성
					.latitude(geoLocation.getContent().getPoint() != null ?
						BigDecimal.valueOf(geoLocation.getContent().getPoint().getY()) :
						locationRequestDTO.getMyLatitude())
					.longitude(geoLocation.getContent().getPoint() != null ?
						BigDecimal.valueOf(geoLocation.getContent().getPoint().getX()) :
						locationRequestDTO.getMyLongitude())
					.build();
			})
			.collect(Collectors.toList());
	}

	// 초기 10개 민들레 생성
	private void createInitialDandelions(Integer userId, DandelionLocationRequestDTO locationRequestDTO) {
		// 사용자 주변에 10개 민들레 랜덤 생성
		Map<Integer, Point> newDandelions = new HashMap<>();
		Random random = new Random();

		for (int i = 0; i < TARGET_DANDELION_COUNT; i++) {
			// 현재 위치에서 100m 이내 랜덤 위치 생성
			double radius = random.nextDouble() * 100; // 0-100m
			double angle = random.nextDouble() * 2 * Math.PI; // 0-360도

			double lat = locationRequestDTO.getMyLatitude().doubleValue();
			double lng = locationRequestDTO.getMyLongitude().doubleValue();

			// 위도 1도 = 약 111km, 경도 1도 = 약 111km * cos(위도)
			double latChange = radius / 111000 * Math.sin(angle);
			double lngChange = radius / (111000 * Math.cos(Math.toRadians(lat))) * Math.cos(angle);

			double newLat = lat + latChange;
			double newLng = lng + lngChange;

			// 새 민들레 엔티티 생성 및 저장
			Dandelion dandelion = Dandelion.builder()
				.userId(null) // 아직 수집되지 않음
				.latitude(BigDecimal.valueOf(newLat))
				.longitude(BigDecimal.valueOf(newLng))
				.build();

			dandelion = dandelionRepository.save(dandelion);

			// Redis에 저장하기 위한 맵에 추가
			newDandelions.put(dandelion.getDandelionId(), new Point(newLng, newLat));
		}

		// Redis에 저장
		dandelionLocationRedisRepository.saveDandelionLocations(userId, newDandelions);
	}

	// 100미터 범위 밖에 있는 민들레를 사용자 주변으로 재배치함
	private void relocateFarDandelions(Integer userId, DandelionLocationRequestDTO locationRequestDTO, int count) {
		// 현재 범위 내에 없는 민들레들 가져오기
		List<Dandelion> farDandelions = dandelionRepository.findByUserId(null);

		// 재배치할 민들레가 충분하지 않으면 새로 생성
		if (farDandelions.size() < count) {
			int needToCreate = count - farDandelions.size();
			createAdditionalDandelions(userId, locationRequestDTO, needToCreate);
		}

		// 재배치할 민들레 맵 생성
		Map<Integer, Point> dandelionsToRelocate = new HashMap<>();
		Random random = new Random();

		for (int i = 0; i < Math.min(count, farDandelions.size()); i++) {
			Dandelion dandelion = farDandelions.get(i);

			// 현재 위치에서 100m 이내 랜덤 위치 생성
			double radius = random.nextDouble() * 100; // 0-100m
			double angle = random.nextDouble() * 2 * Math.PI; // 0-360도

			double lat = locationRequestDTO.getMyLatitude().doubleValue();
			double lng = locationRequestDTO.getMyLongitude().doubleValue();

			// 위도 1도 = 약 111km, 경도 1도 = 약 111km * cos(위도)
			double latChange = radius / 111000 * Math.sin(angle);
			double lngChange = radius / (111000 * Math.cos(Math.toRadians(lat))) * Math.cos(angle);

			double newLat = lat + latChange;
			double newLng = lng + lngChange;

			// 민들레 위치 업데이트
			dandelion.setLatitude(BigDecimal.valueOf(newLat));
			dandelion.setLongitude(BigDecimal.valueOf(newLng));
			dandelionRepository.save(dandelion);

			// Redis에 저장하기 위한 맵에 추가
			dandelionsToRelocate.put(dandelion.getDandelionId(), new Point(newLng, newLat));
		}

		// Redis 위치 정보 업데이트
		dandelionLocationRedisRepository.saveDandelionLocations(userId, dandelionsToRelocate);
	}

	// 민들레 주가 생성
	private void createAdditionalDandelions(Integer userId, DandelionLocationRequestDTO locationRequestDTO, int count) {
		// 부족한 개수만큼 추가 민들레 생성
		Map<Integer, Point> newDandelions = new HashMap<>();
		Random random = new Random();

		for (int i = 0; i < count; i++) {
			// 현재 위치에서 100m 이내 랜덤 위치 생성
			double radius = random.nextDouble() * 100; // 0-100m
			double angle = random.nextDouble() * 2 * Math.PI; // 0-360도

			double lat = locationRequestDTO.getMyLatitude().doubleValue();
			double lng = locationRequestDTO.getMyLongitude().doubleValue();

			// 위도 1도 = 약 111km, 경도 1도 = 약 111km * cos(위도)
			double latChange = radius / 111000 * Math.sin(angle);
			double lngChange = radius / (111000 * Math.cos(Math.toRadians(lat))) * Math.cos(angle);

			double newLat = lat + latChange;
			double newLng = lng + lngChange;

			// 새 민들레 엔티티 생성 및 저장
			Dandelion dandelion = Dandelion.builder()
				.userId(null) // 아직 수집되지 않음
				.latitude(BigDecimal.valueOf(newLat))
				.longitude(BigDecimal.valueOf(newLng))
				.build();

			dandelion = dandelionRepository.save(dandelion);

			// Redis에 저장하기 위한 맵에 추가
			newDandelions.put(dandelion.getDandelionId(), new Point(newLng, newLat));
		}

		// Redis에 저장
		dandelionLocationRedisRepository.saveDandelionLocations(userId, newDandelions);
	}

	// 사용자 위치와 민들레 위치 사이의 거리 계산
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		// 지구 반경 (미터)
		final int R = 6371000;

		// 라디안으로 변환
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		// 하버사인 공식
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// 거리 계산 (미터)
		return R * c;
	}
}
