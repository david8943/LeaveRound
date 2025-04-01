package com.ssafy.Dandelion.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ssafy.Dandelion.domain.dandelion.service.DandelionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

	private final DandelionService dandelionService;

	/**
	 * 매월 1일 오전 1시에 실행되는 스케줄러.
	 * 이전 달의 미수집 황금 민들레를 삭제하고 새 황금 민들레 5개를 생성합니다.
	 */
	@Scheduled(cron = "0 0 1 1 * ?") // 매월 1일 오전 1시
	public void monthlyGoldDandelionReset() {
		log.info("Starting monthly gold dandelion reset task");
		try {
			dandelionService.generateMonthlyGoldDandelions();
			log.info("Monthly gold dandelion reset completed successfully");
		} catch (Exception e) {
			log.error("Error during monthly gold dandelion reset", e);
		}
	}
}
