package com.inno72.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import com.inno72.model.Inno72CheckUser;
import com.inno72.service.CheckUserService;

public class CheckSmsTask {
	@Resource
	private CheckUserService checkUserService;
//	@Scheduled(cron = "0 0 10,11,12,13,14,15,16,17,18,19,20 * * ? ")
	public void sendSmsToCheckUser(){
		Map<String,Object> map = new HashMap<>();
		List<Inno72CheckUser> checkUserList = checkUserService.selectUnReadByParam(map);
		checkUserService.sendSmsToCheck(checkUserList);
	}
}
