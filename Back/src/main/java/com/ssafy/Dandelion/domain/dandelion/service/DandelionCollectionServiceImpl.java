package com.ssafy.Dandelion.domain.dandelion.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.dandelion.DandelionLocationUtil;
import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionLocationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionLocationResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;
import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionLocationRedisRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.GoldDandelionRepository;
import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.DandelionHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DandelionCollectionServiceImpl implements DandelionCollectionService {

	private final DandelionLocationRedisRepository dandelionLocationRedisRepository;
	private final UserRepository userRepository;
	private final DandelionRepository dandelionRepository;
	private final GoldDandelionRepository goldDandelionRepository;
	private final DandelionLocationUtil locationUtil;

	private static final double MAX_COLLECTION_DISTANCE = 50.0;  // 민들레 거리 제한
	private static final int TARGET_DANDELION_COUNT = 10;  // 사용자당 제공할 민들레 개수
	private static final int MONTHLY_GOLD_DANDELIONS = 5;  // 월별 생성할 황금 민들레 개수

	@Override
	@Transactional(readOnly = true)
	public List<DandelionLocationResponseDTO> getPersonalDandelions(Integer userId,
		DandelionLocationRequestDTO locationRequestDTO) {
		// 위치 정보가 유효한지 확인
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		// Redis에서 민들레 위치 목록을 가져오면서 필요시 재배치
		// DB에는 저장하지 않음 (실제 수집 시에만 저장)
		return dandelionLocationRedisRepository.refreshAndGetDandelions(
			userId,
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude(),
			TARGET_DANDELION_COUNT
		);
	}

	// 황금 민들레 조회
	@Override
	@Transactional(readOnly = true)
	public List<GoldDandelionLocationResponseDTO> getUncollectedGoldDandelions() {
		// Redis에서 황금 민들레 목록 조회
		List<Object[]> goldDandelionData = dandelionLocationRedisRepository.getAllGoldDandelionLocations();

		// DTO로 변환하여 반환
		return goldDandelionData.stream()
			.map(data -> {
				Integer id = (Integer)data[0];
				Point point = (Point)data[1];

				// 위도와 경도를 소수점 6자리로 반올림
				BigDecimal latitude = BigDecimal.valueOf(point.getY()).setScale(6, RoundingMode.HALF_UP);
				BigDecimal longitude = BigDecimal.valueOf(point.getX()).setScale(6, RoundingMode.HALF_UP);

				return GoldDandelionLocationResponseDTO.builder()
					.goldDandelionId(id)
					.latitude(latitude)
					.longitude(longitude)
					.build();
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void collectDandelion(Integer userId, Integer dandelionId,
		DandelionLocationRequestDTO locationRequestDTO) {
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		boolean collected = dandelionLocationRedisRepository.collectDandelionIfNearby(
			userId,
			dandelionId,
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude(),
			MAX_COLLECTION_DISTANCE
		);

		if (!collected) {
			throw new DandelionHandler(ErrorStatus.DANDELION_TOO_FAR_TO_COLLECT);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));

		// 사용자의 민들레 개수 업데이트
		user.setDandelionCount(user.getDandelionCount() + 1);
		userRepository.save(user);

		// 수집 시 민들레 정보를 DB에 저장 (dandelionId 포함)
		Dandelion dandelion = Dandelion.builder()
			.dandelionId(dandelionId)
			.userId(userId)
			.latitude(locationRequestDTO.getMyLatitude())
			.longitude(locationRequestDTO.getMyLongitude())
			.build();

		dandelionRepository.save(dandelion);
	}

	@Override
	@Transactional
	public void collectGoldDandelion(Integer userId, Integer goldDandelionId,
		DandelionLocationRequestDTO locationRequestDTO) {
		if (!locationRequestDTO.isValidLocation()) {
			throw new DandelionHandler(ErrorStatus.INVALID_LOCATION);
		}

		boolean collected = dandelionLocationRedisRepository.collectGoldDandelionIfNearby(
			userId,
			goldDandelionId,
			locationRequestDTO.getMyLatitude(),
			locationRequestDTO.getMyLongitude(),
			MAX_COLLECTION_DISTANCE
		);

		if (!collected) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_TOO_FAR_TO_COLLECT);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));

		// 사용자의 황금 민들레 개수 업데이트
		user.setGoldDandelionCount(user.getGoldDandelionCount() + 1);
		userRepository.save(user);

		// 수집 시 황금 민들레 정보를 DB에 저장 (goldDandelionId 포함)
		GoldDandelion goldDandelion = GoldDandelion.builder()
			.goldDandelionId(goldDandelionId)
			.userId(userId)
			.latitude(locationRequestDTO.getMyLatitude())
			.longitude(locationRequestDTO.getMyLongitude())
			.acquiredAt(LocalDateTime.now())
			.build();

		goldDandelionRepository.save(goldDandelion);
	}

	@Override
	@Transactional
	public void generateMonthlyGoldDandelions() {
		// 이전 달의 미수집 황금 민들레 제거
		dandelionLocationRedisRepository.clearUncollectedGoldDandelions();

		// 5개 황금 민들레 생성
		Map<Integer, Point> newGoldDandelions = locationUtil.generateRandomGoldDandelions(
			MONTHLY_GOLD_DANDELIONS
		);

		// Redis에만 저장하고 DB에는 저장하지 않음
		// 실제 사용자가 수집할 때만 DB에 저장
		dandelionLocationRedisRepository.saveGoldDandelionLocations(newGoldDandelions);
	}
}
