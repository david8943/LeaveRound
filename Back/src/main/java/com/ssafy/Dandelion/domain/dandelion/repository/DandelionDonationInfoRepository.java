package com.ssafy.Dandelion.domain.dandelion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.dandelion.entity.DandelionDonationInfo;

@Repository
public interface DandelionDonationInfoRepository extends JpaRepository<DandelionDonationInfo, Long> {

	// 특정 기간 동안의 사용자별 황금 민들레 총 기부 개수, 일반 민들레 총 기부 개수 조회
	@Query(
		"SELECT d.userId, SUM(d.useDandelionCount) as normalDonation, SUM(d.useGoldDandelionCount) as goldDonation " +
			"FROM DandelionDonationInfo d " +
			"WHERE d.createdAt BETWEEN :startDate AND :endDate " +
			"GROUP BY d.userId")
	List<Object[]> findDonationsByTypeInPeriod(@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);

	// 특정 기간 동안의 사용자별 활금 민들레 기부 랭킹 조회(기부 많은 순으로 정렬)
	@Query("SELECT d.userId, SUM(d.useGoldDandelionCount) as totalGoldDonation " +
		"FROM DandelionDonationInfo d " +
		"WHERE d.createdAt BETWEEN :startDate AND :endDate " +
		"GROUP BY d.userId " +
		"ORDER BY totalGoldDonation DESC")
	List<Object[]> findMonthlyGoldDonationRanking(@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
