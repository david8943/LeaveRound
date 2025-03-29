package com.ssafy.Dandelion.domain.dandelion.repository;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;


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

    /**
     * 특정 사용자의 민들레 위치 정보를 Redis에 저장
     * GEOADD 명령어를 사용하여 좌표 정보와 민들레 ID를 저장
     *
     * @param userId 사용자 ID
     * @param dandelionLocations 민들레 ID와 위치(Point) 정보가 담긴 Map
     */
    public void saveDandelionLocations(Integer userId, Map<String, Point> dandelionLocations) {
        String key = getUserDandelionsKey(userId);

        // 각 민들레 ID와 위치 정보를 Redis GEO 자료구조에 저장
        dandelionLocations.forEach((dandelionId, location) ->
                redisTemplate.opsForGeo().add(key, location, dandelionId)
        );
    }

    /**
     * 황금 민들레 위치 정보를 Redis에 저장합니다.
     * GEOADD 명령어를 사용하여 좌표 정보와 황금 민들레 ID를 저장합니다.
     *
     * @param goldDandelionLocations 황금 민들레 ID와 위치(Point) 정보가 담긴 Map
     */
    public void saveGoldDandelionLocations(Map<Integer, Point> goldDandelionLocations) {
        // 각 황금 민들레 ID와 위치 정보를 Redis GEO 자료구조에 저장
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
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> findDandelionsNearby(Integer userId, double latitude, double longitude) {
        String key = getUserDandelionsKey(userId);

        // 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 민들레 검색
        return redisTemplate.opsForGeo().radius(key, new Circle(
                new Point(longitude, latitude), geoDistance
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
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> findGoldDandelionsNearby(double latitude, double longitude) {
        // 주어진 좌표를 중심으로 geoDistance(100m) 반경 내의 모든 황금 민들레 검색
        return redisTemplate.opsForGeo().radius(GOLD_DANDELIONS_KEY, new Circle(
                new Point(longitude, latitude), geoDistance
        ));
    }

    /**
     * 특정 사용자의 모든 민들레 위치를 조회합니다.
     * GEOPOS 명령어를 사용하여 저장된 모든 위치 정보를 가져옵니다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 모든 민들레 위치 목록
     */
    public List<Point> getAllDandelionLocations(Integer userId) {
        String key = getUserDandelionsKey(userId);
        return redisTemplate.opsForGeo().position(key);
    }

    /**
     * 모든 황금 민들레 위치를 조회합니다.
     * GEOPOS 명령어를 사용하여 저장된 모든 황금 민들레 위치 정보를 가져옵니다.
     *
     * @return 모든 황금 민들레 위치 목록
     */
    public List<Point> getAllGoldDandelionLocations() {
        return redisTemplate.opsForGeo().position(GOLD_DANDELIONS_KEY);
    }

    /**
     * 특정 민들레의 위치 정보를 Redis에서 삭제합니다.
     * ZREM 명령어를 사용하여 GEO 자료구조에서 해당 민들레를 제거합니다.
     * 민들레가 수집되었을 때 호출됩니다.
     *
     * @param userId 사용자 ID
     * @param dandelionId 삭제할 민들레 ID
     */
    public void removeDandelion(Integer userId, String dandelionId) {
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
    public void removeGoldDandelion(String dandelionId) {
        redisTemplate.opsForGeo().remove(GOLD_DANDELIONS_KEY, dandelionId);
    }
}
