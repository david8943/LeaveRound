package com.ssafy.Dandelion.domain.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.Dandelion.domain.organization.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
