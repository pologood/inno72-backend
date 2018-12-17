package com.inno72.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.inno72.service.MachineService;

@Configuration
@EnableScheduling
public class CheckSignTask {

	@Resource
	private MachineService machineService;

//	@Scheduled(cron = "0 0/1 * * * ?")
//	@Scheduled(cron = "0 0 20 * * ?")
	public void setSign(){
		machineService.getSignMachineList();
	}
}
