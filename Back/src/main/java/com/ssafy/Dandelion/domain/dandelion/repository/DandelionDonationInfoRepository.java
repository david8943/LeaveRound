package com.ssafy.Dandelion.domain.dandelion.repository;

// 기부 정보 레포지토리
@Repository
public class interface DandelionDonationInfoRepository extends JpaRepository<DandelionDonationInfo, Long> {
    // 특정 기간 동안 기부한 일반 민들레 랭킹 조회
    @Query("SELECT d.userId, SUM(u.useDandelionCount) as totalDonation" +
            "FROM DandelionDonationInfo d" +
            "WHERE d.createdAt BETWEEN :startDate AND :endDate" +
            "GROUP BY d.userId" +
            "ORDER BY totalDonation DESC")

    List<Object[]>



}
