package com.ssafy.Dandelion.domain.dandelion.repository;

import com.ssafy.Dandelion.domain.dandelion.entity.DandelionDonationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DandelionDonationInfoRepository extends JpaRepository<DandelionDonationInfo, Long> {

	@Query("SELECT d.userId, SUM(d.useDandelionCount) + SUM(d.useGoldDandelionCount * 100) AS donationScore " +
			"FROM DandelionDonationInfo d " +
			"WHERE YEARWEEK(d.createdAt) = YEARWEEK(CURRENT_DATE) " + // 현재 주 기준
			"GROUP BY d.userId " +
			"ORDER BY (SUM(d.useDandelionCount) + SUM(d.useGoldDandelionCount * 100)) DESC")
	List<Object[]> getWeeklyDonationRanking();
}
