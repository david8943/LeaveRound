package com.ssafy.Dandelion.domain.dandelion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

@Component
public class DandelionLocationUtil {

	// 한국 육지 범위 좌표 (바다 지역 최소화)
	private static final double MIN_LAT = 33.9; // 남쪽 (제주도 포함)
	private static final double MAX_LAT = 38.0; // 북쪽 (휴전선 이남)
	private static final double MIN_LNG = 126.1; // 서쪽
	private static final double MAX_LNG = 129.5; // 동쪽

	private final Random random = new Random();

	// 사용자 주변에 랜덤 위치 생성
	public Map<Integer, Point> generateRandomDandelions(double centerLat, double centerLng, int count, int baseId) {
		Map<Integer, Point> dandelions = new HashMap<>();

		for (int i = 0; i < count; i++) {
			// 현재 위치에서 100m 이내 랜덤 위치 생성
			double radius = random.nextDouble() * 100; // 0-100m
			double angle = random.nextDouble() * 2 * Math.PI; // 0-360도

			// 위도 1도 = 약 111km, 경도 1도 = 약 111km * cos(위도)
			double latChange = radius / 111000 * Math.sin(angle);
			double lngChange = radius / (111000 * Math.cos(Math.toRadians(centerLat))) * Math.cos(angle);

			double newLat = centerLat + latChange;
			double newLng = centerLng + lngChange;

			// 소수점 6자리로 반올림
			newLat = roundToSixDecimalPlaces(newLat);
			newLng = roundToSixDecimalPlaces(newLng);

			// 고유 ID 생성
			Integer dandelionId = baseId + i;
			dandelions.put(dandelionId, new Point(newLng, newLat));
		}

		return dandelions;
	}

	// 한국 범위 내 황금 민들레 위치 생성
	public Map<Integer, Point> generateRandomGoldDandelions(int count) {
		Map<Integer, Point> goldDandelions = new HashMap<>();
		int baseId = (int)(System.currentTimeMillis() % 1000000);

		for (int i = 0; i < count; i++) {
			// 한국 범위 내 랜덤 위치
			double lat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
			double lng = MIN_LNG + (MAX_LNG - MIN_LNG) * random.nextDouble();

			// 소수점 6자리로 반올림
			lat = roundToSixDecimalPlaces(lat);
			lng = roundToSixDecimalPlaces(lng);

			Integer goldDandelionId = baseId + i;
			goldDandelions.put(goldDandelionId, new Point(lng, lat));
		}

		return goldDandelions;
	}

	// 위도와 경도를 소수점 6자리로 반올림하는 메서드
	private double roundToSixDecimalPlaces(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	// 두 좌표 간 거리 계산 (미터 단위)
	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
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
