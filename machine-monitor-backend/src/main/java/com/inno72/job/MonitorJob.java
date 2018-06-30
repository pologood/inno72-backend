package com.inno72.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitorJob {

	@Scheduled(fixedRate = 1000 * 5)
	public void monitor() {
	}
}
