package com.ssafy.Dandelion.domain.autodonation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;

public interface AutoDonationRepository extends JpaRepository<AutoDonation, Integer> {
	List<AutoDonation> findAllByUserId(Integer userId);

	Boolean existsByAccountNo(String accountNo);

	List<AutoDonation> findAllByIsActiveTrue();
}
