package com.ssafy.Dandelion.global.config;

import com.ssafy.Dandelion.domain.dandelion.service.DandelionCollectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

	private final DandelionCollectionService dandelionCollectionService;

	/**
	 * 매월 1일 오전 1시에 실행되는 스케줄러.
	 * 이전 달의 미수집 황금 민들레를 삭제하고 새 황금 민들레 5개를 생성합니다.
	 */
	@Scheduled(cron = "0 55 17 6 * ?") // 매월 1일 오전 1시(로 변경 필요!!)
	public void monthlyGoldDandelionReset() {
		try {
			dandelionCollectionService.generateMonthlyGoldDandelions();
		} catch (Exception e) {
		}
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("AutoDonationScheduler-");
		scheduler.initialize();
		return scheduler;
	}
}
