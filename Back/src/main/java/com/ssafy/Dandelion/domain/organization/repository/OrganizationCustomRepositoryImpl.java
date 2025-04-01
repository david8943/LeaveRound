package com.ssafy.Dandelion.domain.organization.repository;

import static com.ssafy.Dandelion.domain.organization.entity.QOrganization.organization;
import static com.ssafy.Dandelion.domain.organization.entity.QOrganizationProject.organizationProject;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationRequestDTO;
import com.ssafy.Dandelion.domain.organization.dto.OrganizationResponseDTO;
import com.ssafy.Dandelion.domain.organization.dto.QOrganizationResponseDTO_OrganizationInfo;
import com.ssafy.Dandelion.domain.organization.entity.constant.ProjectCategory;
import com.ssafy.Dandelion.domain.organization.entity.constant.Status;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrganizationCustomRepositoryImpl implements OrganizationCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<OrganizationResponseDTO.OrganizationInfo> findAllOrganizations(
		OrganizationRequestDTO.OrganizationSearchCondition condition) {

		BooleanBuilder builder = new BooleanBuilder();

		if (condition.getKeyword() != null) {
			builder.and(organization.organizationName.containsIgnoreCase(condition.getKeyword()));
		}

		if (condition.getProjectCategory() != null) {
			builder.and(organizationProject.projectCategory.eq(condition.getProjectCategory()));
		}

		return queryFactory
			.select(new QOrganizationResponseDTO_OrganizationInfo(
				organization.organizationId.longValue(),
				organization.organizationName,
				organization.address,
				organization.representative,
				organization.homepageUrl,
				organizationProject.organizationProjectId.longValue(),
				organizationProject.goalAmount,
				organizationProject.totalUseAmount,
				organizationProject.currentAmount,
				organizationProject.title,
				organizationProject.content,
				getProjectCategoryExpression(),
				getStatusExpression(),
				organization.createdAt,
				organization.updatedAt,
				organizationProject.endDatedAt
			))
			.from(organization)
			.leftJoin(organizationProject)
			.on(organizationProject.organizationId.eq(organization.organizationId))
			.where(builder)
			.fetch();
	}

	@Override
	public OrganizationResponseDTO.OrganizationInfo getOrganizationInfo(Integer organizationProjectId) {
		return queryFactory
			.select(new QOrganizationResponseDTO_OrganizationInfo(
				organization.organizationId.longValue(),
				organization.organizationName,
				organization.address,
				organization.representative,
				organization.homepageUrl,
				organizationProject.organizationProjectId.longValue(),
				organizationProject.goalAmount,
				organizationProject.totalUseAmount,
				organizationProject.currentAmount,
				organizationProject.title,
				organizationProject.content,
				getProjectCategoryExpression(),
				getStatusExpression(),
				organization.createdAt,
				organization.updatedAt,
				organizationProject.endDatedAt
			))
			.from(organization)
			.leftJoin(organizationProject)
			.on(organizationProject.organizationId.eq(organization.organizationId))
			.where(organizationProject.organizationProjectId.eq(organizationProjectId))
			.fetchOne();
	}

	private Expression<String> getProjectCategoryExpression() {
		return new CaseBuilder()
			.when(organizationProject.projectCategory.eq(ProjectCategory.ENVIRONMENT))
			.then("환경")
			.when(organizationProject.projectCategory.eq(ProjectCategory.HEALTHCARE))
			.then("보건")
			.when(organizationProject.projectCategory.eq(ProjectCategory.EDUCATION))
			.then("교육")
			.otherwise("기타");
	}

	private Expression<String> getStatusExpression() {
		return new CaseBuilder()
			.when(organizationProject.status.eq(Status.ONGOING))
			.then("진행중")
			.when(organizationProject.status.eq(Status.COMPLETED))
			.then("종료")
			.otherwise("기타");
	}
}
