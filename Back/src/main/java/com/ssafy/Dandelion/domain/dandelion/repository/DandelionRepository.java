package com.ssafy.Dandelion.domain.dandelion.repository;

import java.util.List;

import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// 일반 민들레 레포지토리
@Repository
public interface DandelionRepository extends JpaRepository<Dandelion, Integer> {

	// 사용자가 갖고 있는 모든 일반 민들레 조회
	List<Dandelion> findByUserId(Integer userId);

	// 사용자가 갖고 있는 일반 민들레 총 개수
	@Query("SELECT COUNT(d) FROM Dandelion d WHERE d.userId = :userId")
	int countByUserId(@Param("userId")Integer userId);
}
