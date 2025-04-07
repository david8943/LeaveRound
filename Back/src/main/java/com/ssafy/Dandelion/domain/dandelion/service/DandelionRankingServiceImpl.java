package com.ssafy.Dandelion.domain.dandelion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.Dandelion.domain.dandelion.dto.response.DandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.GoldDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MonthlyGoldRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MyDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.MyGoldDandelionRankDTO;
import com.ssafy.Dandelion.domain.dandelion.dto.response.WeeklyRankingResponseDTO;
import com.ssafy.Dandelion.domain.dandelion.repository.DandelionDonationInfoRepository;
import com.ssafy.Dandelion.domain.dandelion.repository.GoldDandelionRepository;
import com.ssafy.Dandelion.domain.user.entity.User;
import com.ssafy.Dandelion.domain.user.repository.UserRepository;
import com.ssafy.Dandelion.global.apiPayload.code.status.ErrorStatus;
import com.ssafy.Dandelion.global.apiPayload.exception.handler.NotFoundHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DandelionRankingServiceImpl implements DandelionRankingService {

	private final DandelionDonationInfoRepository donationInfoRepository;
	private final GoldDandelionRepository goldDandelionRepository;
	private final UserRepository userRepository;

	private static final int WEEKLY_RANKING_LIMIT = 10;

	@Override
	public WeeklyRankingResponseDTO getWeeklyRanking(Integer userId) {
		// DB에서 주간 기부 랭킹 데이터 조회
		// getWeeklyRanking 메소드 내부에서
		List<Object[]> donationRankingData = donationInfoRepository.getWeeklyDonationRanking();
		log.info("Donation Ranking Data: {}", donationRankingData);

		List<DandelionRankDTO> rankInfos = new ArrayList<>();
		MyDandelionRankDTO myRankInfo = null;

		// Handle empty results
		if (donationRankingData == null || donationRankingData.isEmpty()) {
			User currentUser = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));

			myRankInfo = MyDandelionRankDTO.builder()
				.myRank(0)
				.myName(currentUser.getName())
				.myDonateCount(0)
				.build();

			return WeeklyRankingResponseDTO.builder()
				.rankInfos(new ArrayList<>())
				.myRankInfo(myRankInfo)
				.build();
		}

		List<Object[]> sortedEntries = donationRankingData.stream()
			.filter(entry -> entry.length >= 2 && entry[1] != null)
			.filter(entry -> {
				try {
					Number score = (Number)entry[1];
					return score.longValue() > 0;
				} catch (ClassCastException e) {
					log.error("Failed to cast donation score for entry: {}", entry, e);
					return false;
				}
			})
			.sorted((entry1, entry2) -> {
				try {
					Number score1 = (Number)entry1[1];
					Number score2 = (Number)entry2[1];
					return Long.compare(score2.longValue(), score1.longValue());
				} catch (ClassCastException e) {
					log.error("Failed to compare entries: {} and {}", entry1, entry2, e);
					return 0;
				}
			})
			.collect(Collectors.toList());

		log.info("Sorted Entries: {}", sortedEntries);

		// 상위 10명 랭킹 생성
		int rank = 1;
		int limit = Math.min(WEEKLY_RANKING_LIMIT, sortedEntries.size());

		for (int i = 0; i < limit; i++) {
			Object[] entry = sortedEntries.get(i);
			try {
				Integer donorId = (Integer)entry[0];
				Number donationScore = (Number)entry[1];
				int score = donationScore.intValue();

				// 동점자 처리
				if (i > 0) {
					Object[] prevEntry = sortedEntries.get(i - 1);
					Number prevScore = (Number)prevEntry[1];
					if (score == prevScore.intValue()) {
						// 이전 사용자와 동일한 랭크 부여
					} else {
						rank = i + 1;
					}
				}

				// 사용자 이름 조회
				String userName = userRepository.findById(donorId)
					.map(User::getName)
					.orElse("Unknown User");

				DandelionRankDTO rankDTO = DandelionRankDTO.builder()
					.rank(rank)
					.name(userName)
					.donateCount(score)
					.build();

				rankInfos.add(rankDTO);

				// 현재 사용자 정보 저장
				if (donorId.equals(userId)) {
					myRankInfo = MyDandelionRankDTO.builder()
						.myRank(rank)
						.myName(userName)
						.myDonateCount(score)
						.build();
				}
			} catch (ClassCastException e) {
				log.error("Failed to process entry: {}", entry, e);
				// Skip this entry and continue with the next one
			}
		}

		// 사용자가 랭킹에 없는 경우
		if (myRankInfo == null) {
			User currentUser = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundHandler(ErrorStatus.MEMBER_NOT_FOUND));

			myRankInfo = MyDandelionRankDTO.builder()
				.myRank(0)
				.myName(currentUser.getName())
				.myDonateCount(0)
				.build();
		}

		return WeeklyRankingResponseDTO.builder()
			.rankInfos(rankInfos)
			.myRankInfo(myRankInfo)
			.build();
	}

	@Override
	public MonthlyGoldRankingResponseDTO getMonthlyGoldRanking(Integer userId) {
		// DB에서 월간 황금 민들레 보유 및 기부한 수 합산 랭킹 데이터 조회
		List<Object[]> goldRankingData = goldDandelionRepository.getMonthlyGoldDonationRanking();
		log.info("Gold Ranking Data: {}", goldRankingData);

		List<GoldDandelionRankDTO> goldInfos = new ArrayList<>();
		MyGoldDandelionRankDTO myGoldInfo = null;

		// 황금 민들레 보유 + 기부 수 내림차순으로 정렬
		for (Object[] entry : goldRankingData) {
			try {
				Integer userIdValue = (Integer)entry[0];
				Number totalGoldCount = (Number)entry[1]; // Number 타입으로 처리
				int goldCount = totalGoldCount.intValue();

				if (goldCount <= 0) {
					continue;
				}

				String userName = userRepository.findById(userIdValue)
					.map(User::getName)
					.orElse("Unknown User");

				GoldDandelionRankDTO rankDTO = GoldDandelionRankDTO.builder()
					.name(userName)
					.goldCount(goldCount)
					.build();

				goldInfos.add(rankDTO);

				// 현재 사용자 정보 저장
				if (userIdValue.equals(userId)) {
					myGoldInfo = MyGoldDandelionRankDTO.builder()
						.myName(userName)
						.myGoldCount(goldCount)
						.build();
				}
			} catch (Exception e) {
				log.error("Error processing gold ranking entry: {}", entry, e);
			}
		}

		// 만약 데이터가 없거나 문제가 있는 경우를 위한 디버깅 로그
		if (goldInfos.isEmpty()) {
			log.warn("No gold ranking data found or all entries were filtered out");
		}

		// 랭킹에 없는 경우 기본값 설정
		if (myGoldInfo == null) {
			String userName = userRepository.findById(userId)
				.map(User::getName)
				.orElse("Unknown User");

			myGoldInfo = MyGoldDandelionRankDTO.builder()
				.myName(userName)
				.myGoldCount(0)
				.build();
		}

		return MonthlyGoldRankingResponseDTO.builder()
			.goldInfos(goldInfos)
			.myGoldInfo(myGoldInfo)
			.build();
	}
}
