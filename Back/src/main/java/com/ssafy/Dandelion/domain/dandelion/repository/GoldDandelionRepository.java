package com.ssafy.Dandelion.domain.dandelion.repository;

import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoldDandelionRepository extends JpaRepository<GoldDandelion, Integer> {

    // 월간 황금 민들레 보유 및 기부한 수 합산 랭킹 조회
    @Query(value =
            "SELECT u.user_id, COALESCE(u.gold_dandelion_count, 0) + COALESCE(u.gold_dandelion_use_count, 0) as total_gold " +
                    "FROM users u " +
                    "WHERE (u.gold_dandelion_count > 0 OR u.gold_dandelion_use_count > 0) " +
                    "ORDER BY total_gold DESC", nativeQuery = true)
    List<Object[]> getMonthlyGoldDonationRanking();
}
