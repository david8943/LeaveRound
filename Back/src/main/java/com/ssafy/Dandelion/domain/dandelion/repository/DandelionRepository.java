package com.ssafy.Dandelion.domain.dandelion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;

// 일반 민들레 레포지토리
@Repository
public interface DandelionRepository extends JpaRepository<Dandelion, Integer> {
	/**
	 * 특정 사용자의 모든 민들레를 조회하는 메소드
	 *
	 * @param userId 조회할 사용자의 ID
	 * @return 해당 사용자의 민들레 목록
	 */
	List<Dandelion> findByUserId(Integer userId);
	
}
