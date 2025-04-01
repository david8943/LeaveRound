package com.ssafy.Dandelion.domain.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.organization.entity.OrganizationProject;

@Repository
public interface OrganizationProjectRepository
	extends JpaRepository<OrganizationProject, Integer>, OrganizationCustomRepository {

}
