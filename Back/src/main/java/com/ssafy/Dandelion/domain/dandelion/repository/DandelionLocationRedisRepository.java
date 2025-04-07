package com.ssafy.Dandelion.domain.dandelion.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.dandelion.DandelionLocationUtil;
import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionLocationResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DandelionLocationRedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final Distance geoDistance;
	private final DandelionLocationUtil locationUtil;

	// Redis 키 관련 상수
	private static final String USER_DANDELIONS_PREFIX = "user:%d:dandelions";
	private static final String USER_COLLECTED_DANDELIONS_PREFIX = "user:%d:collected:dandelions";
	private static final String GOLD_DANDELIONS_KEY = "gold:dandelions";
	private static final String USER_COLLECTED_GOLD_PREFIX = "user:%d:collected:gold";
	private static final String WEEKLY_DONATION_PREFIX = "donations:weekly:%s";
	private static final String MONTHLY_GOLD_PREFIX = "donations:gold:monthly:%s";

	// 사용자 주변 민들레 조회 및 필요시 재배치
	public List<DandelionLocationResponseDTO> refreshAndGetDandelions(Integer userId,
		BigDecimal latitude,
		BigDecimal longitude,
		int targetCount) {
		String userKey = String.format(USER_DANDELIONS_PREFIX, userId);

		// 현재 시간 기반 타임스탬프 (ID 생성용)
		long timestamp = System.currentTimeMillis();

		// 주변 민들레 검색
		List<DandelionLocationResponseDTO> locationDTOs = new ArrayList<>();

		try {
			// 100m 반경 내의 민들레 검색
			GeoResults<RedisGeoCommands.GeoLocation<Object>> nearbyDandelions =
				redisTemplate.opsForGeo().radius(userKey,
					new Circle(
						new Point(longitude.doubleValue(), latitude.doubleValue()),
						new Distance(100, RedisGeoCommands.DistanceUnit.METERS)
					),
					RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates()
				);

			// 검색된 민들레를 DTO로 변환
			if (nearbyDandelions != null && nearbyDandelions.getContent() != null) {
				nearbyDandelions.getContent().forEach(geoLocation -> {
					if (locationDTOs.size() < targetCount) {
						locationDTOs.add(DandelionLocationResponseDTO.builder()
							.dandelionId((Integer)geoLocation.getContent().getName())
							.latitude(BigDecimal.valueOf(geoLocation.getContent().getPoint().getY()))
							.longitude(BigDecimal.valueOf(geoLocation.getContent().getPoint().getX()))
							.build());
					}
				});
			}

			// 민들레가 부족한 경우 추가 생성
			if (locationDTOs.size() < targetCount) {
				int needToCreate = targetCount - locationDTOs.size();

				// 새로운 민들레 생성
				Map<Integer, Point> newDandelions = locationUtil.generateRandomDandelions(
					latitude.doubleValue(),
					longitude.doubleValue(),
					needToCreate,
					(int)(timestamp % 1000000)
				);

				// Redis에 저장
				saveDandelionLocations(userId, newDandelions);

				// 결과 목록에 추가
				for (Map.Entry<Integer, Point> entry : newDandelions.entrySet()) {
					Point point = entry.getValue();
					locationDTOs.add(DandelionLocationResponseDTO.builder()
						.dandelionId(entry.getKey())
						.latitude(BigDecimal.valueOf(point.getY()))
						.longitude(BigDecimal.valueOf(point.getX()))
						.build());
				}
			}

			return locationDTOs;
		} catch (Exception e) {
			log.error("Error fetching dandelions from Redis", e);

			// 오류 발생 시 간단한 방식으로 처리
			// 모든 민들레 삭제 후 새로 생성
			redisTemplate.delete(userKey);

			Map<Integer, Point> newDandelions = locationUtil.generateRandomDandelions(
				latitude.doubleValue(),
				longitude.doubleValue(),
				targetCount,
				(int)(timestamp % 1000000)
			);

			saveDandelionLocations(userId, newDandelions);

			return newDandelions.entrySet().stream()
				.map(entry -> DandelionLocationResponseDTO.builder()
					.dandelionId(entry.getKey())
					.latitude(BigDecimal.valueOf(entry.getValue().getY()))
					.longitude(BigDecimal.valueOf(entry.getValue().getX()))
					.build())
				.toList();
		}
	}

	// 모든 황금 민들레 위치 조회
	public List<Object[]> getAllGoldDandelionLocations() {
		Set<Object> allGoldDandelionIds = redisTemplate.opsForZSet().range(GOLD_DANDELIONS_KEY, 0, -1);
		List<Object[]> result = new ArrayList<>();

		if (allGoldDandelionIds != null && !allGoldDandelionIds.isEmpty()) {
			List<Point> positions = redisTemplate.opsForGeo().position(GOLD_DANDELIONS_KEY,
				allGoldDandelionIds.toArray());

			int i = 0;
			for (Object id : allGoldDandelionIds) {
				if (i < positions.size() && positions.get(i) != null) {
					result.add(new Object[] {id, positions.get(i)});
				}
				i++;
			}
		}

		return result;
	}

	// 민들레 수집 (거리 확인 포함)
	public boolean collectDandelionIfNearby(Integer userId, Integer dandelionId,
		BigDecimal latitude, BigDecimal longitude,
		double maxDistance) {
		String userKey = String.format(USER_DANDELIONS_PREFIX, userId);
		String collectedKey = String.format(USER_COLLECTED_DANDELIONS_PREFIX, userId);

		try {
			// 민들레 위치 정보 가져오기
			List<Point> positions = redisTemplate.opsForGeo().position(userKey, dandelionId);

			if (positions == null || positions.isEmpty() || positions.get(0) == null) {
				return false;
			}

			Point dandelionPoint = positions.get(0);

			// 거리 계산
			double distance = locationUtil.calculateDistance(
				latitude.doubleValue(),
				longitude.doubleValue(),
				dandelionPoint.getY(),
				dandelionPoint.getX()
			);

			if (distance > maxDistance) {
				return false;
			}

			// 수집 처리
			redisTemplate.opsForGeo().add(collectedKey, dandelionPoint, dandelionId);
			redisTemplate.opsForZSet().remove(userKey, dandelionId);

			return true;
		} catch (Exception e) {
			log.error("Error collecting dandelion", e);
			return false;
		}
	}

	// 황금 민들레 수집 (거리 확인 포함)
	public boolean collectGoldDandelionIfNearby(Integer userId, Integer goldDandelionId,
		BigDecimal latitude, BigDecimal longitude,
		double maxDistance) {
		String collectedKey = String.format(USER_COLLECTED_GOLD_PREFIX, userId);

		// 위치 정보 가져오기
		List<Point> positions = redisTemplate.opsForGeo().position(GOLD_DANDELIONS_KEY, goldDandelionId);

		if (positions == null || positions.isEmpty() || positions.get(0) == null) {
			return false;
		}

		Point goldPoint = positions.get(0);

		// 거리 계산
		double distance = locationUtil.calculateDistance(
			latitude.doubleValue(),
			longitude.doubleValue(),
			goldPoint.getY(),
			goldPoint.getX()
		);

		if (distance > maxDistance) {
			return false;
		}

		// 수집 처리
		redisTemplate.opsForGeo().add(collectedKey, goldPoint, goldDandelionId);
		redisTemplate.opsForZSet().remove(GOLD_DANDELIONS_KEY, goldDandelionId);

		return true;
	}

	// 민들레 위치 저장
	public void saveDandelionLocations(Integer userId, Map<Integer, Point> dandelionLocations) {
		String key = String.format(USER_DANDELIONS_PREFIX, userId);

		dandelionLocations.forEach((dandelionId, location) ->
			redisTemplate.opsForGeo().add(key, location, dandelionId)
		);
	}

	// 황금 민들레 위치 저장
	public void saveGoldDandelionLocations(Map<Integer, Point> goldDandelionLocations) {
		goldDandelionLocations.forEach((dandelionId, location) ->
			redisTemplate.opsForGeo().add(GOLD_DANDELIONS_KEY, location, dandelionId)
		);

		// 1달 후 만료 설정
		redisTemplate.expire(GOLD_DANDELIONS_KEY, 31, TimeUnit.DAYS);
	}

	// 미수집 황금 민들레 삭제
	public void clearUncollectedGoldDandelions() {
		redisTemplate.delete(GOLD_DANDELIONS_KEY);
	}

}
