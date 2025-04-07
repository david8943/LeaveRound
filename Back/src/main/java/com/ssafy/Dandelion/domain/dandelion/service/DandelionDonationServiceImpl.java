package com.ssafy.Dandelion.domain.dandelion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.Dandelion.domain.dandelion.dto.request.DandelionDonationRequestDTO;
import com.ssafy.Dandelion.domain.dandelion.entity.DandelionDonationInfo;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionDonationInfoRepository;
import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.DandelionHandler;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DandelionDonationServiceImpl implements DandelionDonationService {

	private final DandelionDonationInfoRepository donationInfoRepository;
	private final UserRepository userRepository;

	// 민들레 기부
	@Override
	@Transactional
	public void donateDandelions(Integer userId, DandelionDonationRequestDTO donationRequestDTO) {
		// 기부할 민들레 개수 확인
		int normalCount = donationRequestDTO.getDandelionCount() != null ? donationRequestDTO.getDandelionCount() : 0;
		int goldCount =
			donationRequestDTO.getGoldDandelionCount() != null ? donationRequestDTO.getGoldDandelionCount() : 0;

		// 기부할 민들레가 없는 경우 예외 처리
		if (normalCount == 0 && goldCount == 0) {
			throw new DandelionHandler(ErrorStatus.EMPTY_DONATION);
		}

		// 사용자 정보 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));

		// 기부 가능한 민들레 개수 확인
		int availableNormal = user.getDandelionCount();
		int availableGold = user.getGoldDandelionCount();

		// 사용자가 가진 민들레보다 많은 개수를 기부하려는 경우 예외 처리
		if (normalCount > 0 && normalCount > availableNormal) {
			throw new DandelionHandler(ErrorStatus.DANDELION_OVER_COUNT);
		}

		if (goldCount > 0 && goldCount > availableGold) {
			throw new DandelionHandler(ErrorStatus.GOLD_DANDELION_OVER_COUNT);
		}

		// 기부처 ID 처리
		Integer projectId = donationRequestDTO.getProjectId();
		if (projectId == null || projectId == 0) {
			// 기본 기부처 설정
			projectId = 1;
		}

		// 기부 정보 생성 및 저장
		DandelionDonationInfo donationInfo = DandelionDonationInfo.builder()
			.userId(userId)
			.organizationProjectId(projectId)
			.useDandelionCount(normalCount)
			.useGoldDandelionCount(goldCount)
			.build();

		// 기부 정보를 DandelionDonationInfo 테이블에 저장 - 반환값은 무시
		donationInfoRepository.save(donationInfo);

		// 일반 민들레 보유 개수 갱신
		user.setDandelionCount(Math.max(0, user.getDandelionCount() - normalCount));

		// 황금 민들레 보유 개수 갱신
		user.setGoldDandelionCount(Math.max(0, user.getGoldDandelionCount() - goldCount));

		// 사용한 민들레 개수 증가
		user.setDandelionUseCount(user.getDandelionUseCount() + normalCount);
		user.setGoldDandelionUseCount(user.getGoldDandelionUseCount() + goldCount);

		userRepository.save(user);
	}

	@Override
	public int getAvailableDandelionCount(Integer userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));
		return user.getDandelionCount();
	}

	@Override
	public int getAvailableGoldDandelionCount(Integer userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));
		return user.getGoldDandelionCount();
	}
}
