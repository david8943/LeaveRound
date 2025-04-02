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
public class DandelionLocationRedisRepository {

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

	// 사용자 위치 주변의 일반 민들레를 검색
	public GeoResults<RedisGeoCommands.GeoLocation<Object>> findDandelionsNearby(Integer userId, BigDecimal latitude,
		BigDecimal longitude) {
		String key = getUserDandelionsKey(userId);

		// 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 민들레 검색
		return redisTemplate.opsForGeo().radius(key, new Circle(
			new Point(longitude.doubleValue(), latitude.doubleValue()), geoDistance));
	}

	// 사용자 위치 주변의 황금 민들레 검색
	public GeoResults<RedisGeoCommands.GeoLocation<Object>> findGoldDandelionsNearby(BigDecimal latitude,
		BigDecimal longitude) {
		// 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 황금 민들레 검색
		return redisTemplate.opsForGeo().radius(GOLD_DANDELIONS_KEY, new Circle(
			new Point(longitude.doubleValue(), latitude.doubleValue()), geoDistance
		));
	}

	// 수집 가능한 황금 민들레 조회
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

	// 일반 민들레가 수집 된 후 삭제하기
	public void removeDandelion(Integer userId, Integer dandelionId) {
		String key = getUserDandelionsKey(userId);
		redisTemplate.opsForGeo().remove(key, dandelionId);
	}

	// 황금 민들레 수집된 후 삭제하기
	public void removeGoldDandelion(Integer dandelionId) {
		redisTemplate.opsForGeo().remove(GOLD_DANDELIONS_KEY, dandelionId);
	}
}
