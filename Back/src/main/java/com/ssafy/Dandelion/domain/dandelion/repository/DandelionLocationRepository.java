package com.ssafy.Dandelion.domain.dandelion.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DandelionLocationRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final Distance geoDistance; //100미터 거리 설정 됨

	// 사용자의 민들레 위치 데이터를 저장할 redis 키
	private String getUserDandelionsKey(Integer userId) {
		return "user:" + userId + ":dandelions";
	}

	// 황금 민들레 redis 키
	private final String GOLD_DANDELIONS_KEY = "gold:dandelions";

	// 사용자 위치 정보 redis에 add로 저장
	public void saveDandelionLocations(Integer userId, Map<Integer, Point> dandelionLocations) {
		String key = getUserDandelionsKey(userId);

		// 각 민들레 ID와 위치 정보를 Redis에 저장
		dandelionLocations.forEach((dandelionId, location) ->
			redisTemplate.opsForGeo().add(key, location, dandelionId)
		);
	}

	// 황금 민들레 위치 redis에 저장
	public void saveGoldDandelionLocations(Map<Integer, Point> goldDandelionLocations) {
		// 각 황금 민들레 ID와 위치 정보를 Redis에 저장
		goldDandelionLocations.forEach((dandelionId, location) ->
			redisTemplate.opsForGeo().add(GOLD_DANDELIONS_KEY, location, dandelionId)
		);
	}

	/**
	 * 특정 위치 주변의 민들레를 검색합니다.
	 * GEORADIUS 명령어를 사용하여 지정된 반경(geoDistance) 내의 민들레를 찾습니다.
	 *
	 * @param userId 사용자 ID
	 * @param latitude 기준 위도
	 * @param longitude 기준 경도
	 * @return 지정된 반경 내의 민들레 위치 정보 결과 목록
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<Object>> findDandelionsNearby(Integer userId, BigDecimal latitude,
		BigDecimal longitude) {
		String key = getUserDandelionsKey(userId);

		// 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 민들레 검색
		return redisTemplate.opsForGeo().radius(key, new Circle(
			new Point(longitude.doubleValue(), latitude.doubleValue()), geoDistance
		));
	}

	/**
	 * 특정 위치 주변의 황금 민들레를 검색합니다.
	 * GEORADIUS 명령어를 사용하여 지정된 반경(geoDistance) 내의 황금 민들레를 찾습니다.
	 *
	 * @param latitude 기준 위도
	 * @param longitude 기준 경도
	 * @return 지정된 반경 내의 황금 민들레 위치 정보 결과 목록
	 */
	public GeoResults<RedisGeoCommands.GeoLocation<Object>> findGoldDandelionsNearby(BigDecimal latitude,
		BigDecimal longitude) {
		// 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 황금 민들레 검색
		return redisTemplate.opsForGeo().radius(GOLD_DANDELIONS_KEY, new Circle(
			new Point(longitude.doubleValue(), latitude.doubleValue()), geoDistance
		));
	}

	/**
	 * 특정 사용자의 수집 가능한 모든 민들레 위치를 조회합니다.
	 * Redis에 저장된 해당 사용자의 모든 민들레 ID와 위치 정보를 가져옵니다.
	 *
	 * @param userId 사용자 ID
	 * @return 해당 사용자가 수집할 수 있는 모든 민들레 위치 목록
	 */
	public List<Point> getAllCollectableDandelionLocations(Integer userId) {
		String key = getUserDandelionsKey(userId);

		// 모든 민들레 ID 목록 조회 (ZRANGE 사용)
		Set<Object> allDandelionIds = redisTemplate.opsForZSet().range(key, 0, -1);

		if (allDandelionIds == null || allDandelionIds.isEmpty()) {
			return new ArrayList<>();
		}

		// 각 민들레 ID의 위치 정보 조회 (GEOPOS 사용)
		List<Point> positions = redisTemplate.opsForGeo().position(key, allDandelionIds.toArray());
		return positions != null ? positions : new ArrayList<>();
	}

	/**
	 * 모든 수집 가능한 황금 민들레 위치를 조회합니다.
	 * Redis에 저장된 모든 황금 민들레 ID와 위치 정보를 가져옵니다.
	 *
	 * @return 모든 수집 가능한 황금 민들레 위치 목록
	 */
	public List<Point> getAllCollectableGoldDandelionLocations() {
		// 모든 황금 민들레 ID 목록 조회 (ZRANGE 사용)
		Set<Object> allGoldDandelionIds = redisTemplate.opsForZSet().range(GOLD_DANDELIONS_KEY, 0, -1);

		if (allGoldDandelionIds == null || allGoldDandelionIds.isEmpty()) {
			return new ArrayList<>();
		}

		// 각 황금 민들레 ID의 위치 정보 조회 (GEOPOS 사용)
		List<Point> positions = redisTemplate.opsForGeo().position(GOLD_DANDELIONS_KEY, allGoldDandelionIds.toArray());
		return positions != null ? positions : new ArrayList<>();
	}

	/**
	 * 특정 민들레의 위치 정보를 Redis에서 삭제합니다.
	 * ZREM 명령어를 사용하여 GEO 자료구조에서 해당 민들레를 제거합니다.
	 * 민들레가 수집되었을 때 호출됩니다.
	 *
	 * @param userId 사용자 ID
	 * @param dandelionId 삭제할 민들레 ID
	 */
	public void removeDandelion(Integer userId, Integer dandelionId) {
		String key = getUserDandelionsKey(userId);
		redisTemplate.opsForGeo().remove(key, dandelionId);
	}

	/**
	 * 특정 황금 민들레의 위치 정보를 Redis에서 삭제합니다.
	 * ZREM 명령어를 사용하여 GEO 자료구조에서 해당 황금 민들레를 제거합니다.
	 * 황금 민들레가 수집되었을 때 호출됩니다.
	 *
	 * @param dandelionId 삭제할 황금 민들레 ID
	 */
	public void removeGoldDandelion(Integer dandelionId) {
		redisTemplate.opsForGeo().remove(GOLD_DANDELIONS_KEY, dandelionId);
	}
}
