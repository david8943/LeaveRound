package com.ssafy.Dandelion.domain.autodonation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonationInfo;

public interface AutoDonationInfoRepository extends JpaRepository<AutoDonationInfo, Integer> {
	List<AutoDonationInfo> findAllByAutoDonationId(Integer autoDonationId);
}
