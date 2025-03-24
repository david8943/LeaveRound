package com.ssafy.Dandelion.domain.autodonation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;

public interface AutoDonationRepository extends JpaRepository<AutoDonation, Integer> {
}
