package com.ssafy.Dandelion.domain.dandelion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;

@Repository
public interface GoldDandelionRepository extends JpaRepository<GoldDandelion, Integer> {

	// 특정 기간 내에 아직 수집되지 않은 황금 민들레 조회
	@Query("SELECT g FROM GoldDandelion g WHERE g.userId IS NULL AND g.createdAt BETWEEN :startDate AND :endDate")
	List<GoldDandelion> findUncollectedGoldDandelionsBetween(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);

	// 특정 기간에 사용자가 수집한 황금 민들레 개수를 조회 (acquiredAt 기준)
	@Query("SELECT g.userId, COUNT(g) FROM GoldDandelion g WHERE g.userId IS NOT NULL AND g.acquiredAt BETWEEN :startDate AND :endDate GROUP BY g.userId ORDER BY COUNT(g) DESC")
	List<Object[]> findGoldDandelionCollectionRankingBetween(
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
