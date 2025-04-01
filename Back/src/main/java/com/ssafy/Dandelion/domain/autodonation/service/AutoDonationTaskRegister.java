package com.ssafy.Dandelion.domain.autodonation.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.ssafy.Dandelion.domain.autodonation.entity.AutoDonation;
import com.ssafy.Dandelion.domain.autodonation.repository.AutoDonationRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoDonationTaskRegister {

	private final TaskScheduler taskScheduler; // 스프링 스케줄러
	private final AutoDonationRepository autoDonationRepository;

	private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		// 서버 시작 시, 모든 active AutoDonation 로드
		List<AutoDonation> autoDonations = autoDonationRepository.findAllByIsActiveTrue();

		for (AutoDonation autoDonation : autoDonations) {
			scheduleAutoDonation(autoDonation);
		}
	}

	public void scheduleAutoDonation(AutoDonation autoDonation) {
		long interval = 10;
		//autoDonation.getDonateTime().getDays() * 24 * 60 * 60 * 1000L;

		Runnable task = () -> {
			try {
				//autoDonationService.executeAutoDonation(autoDonation);
			} catch (Exception e) {
				log.error("AutoDonation 실패: userId={}, 이유={}", autoDonation.getUserId(), e.getMessage());
			}
		};

		ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(task, interval);
		scheduledTasks.put(autoDonation.getAutoDonationId(), scheduledFuture);
	}

	public void cancelAutoDonation(Integer autoDonationId) {
		ScheduledFuture<?> future = scheduledTasks.remove(autoDonationId);
		if (future != null) {
			future.cancel(false);
		}
	}

	public void restartAutoDonation(AutoDonation autoDonation) {
		cancelAutoDonation(autoDonation.getAutoDonationId());
		scheduleAutoDonation(autoDonation);
	}
}
