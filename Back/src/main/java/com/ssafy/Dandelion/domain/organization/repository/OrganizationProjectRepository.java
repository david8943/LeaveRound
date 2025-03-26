package com.ssafy.Dandelion.domain.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.Dandelion.domain.organization.entity.OrganizationProject;

public interface OrganizationProjectRepository extends JpaRepository<OrganizationProject, Integer> {
}
