package com.ssafy.Dandelion.domain.dandelion.repository;

import com.ssafy.Dandelion.domain.dandelion.entity.GoldDandelion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoldDandelionRepository extends JpaRepository<GoldDandelion, Integer> {

    // 사용자가 갖고 있는 모든 황금 민들레 조회
    List<GoldDandelion> findByUserId(Integer userId);

    // 사용자가 갖고 있는 황금 민들레 총 개수
    @Query("SELECT COUNT(g) FROM GoldDandelion g WHERE g.userId = :userId")
    int countByUserId(@Param("userId") Integer userId);

    // 이번달에 아직 수집되지 않은 황금 민들레 조회
    @Query("SELECT g FROM GoldDandelion g WHERE g.userId IS NULL AND g.createdAt >= :startOfMonth")
    List<GoldDandelion> findAvailableGoldDandelionsThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth);

    // 매월 1일에 지난 달의 미수집 황금 민들레 삭제하기 위한 메서드
    List<GoldDandelion> findByUserIdIsNullAndCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
