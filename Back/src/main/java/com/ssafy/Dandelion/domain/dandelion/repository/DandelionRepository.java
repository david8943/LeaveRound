package com.ssafy.Dandelion.domain.dandelion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.dandelion.entity.Dandelion;

// 일반 민들레 레포지토리
@Repository
public interface DandelionRepository extends JpaRepository<Dandelion, Integer> {

	// 사용자가 갖고 있는 모든 일반 민들레 조회
	List<Dandelion> findByUserId(Integer userId);
}
