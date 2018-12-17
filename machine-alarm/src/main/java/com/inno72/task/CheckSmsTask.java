package com.inno72.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.job.core.biz.model.ReturnT;
import com.inno72.job.core.handle.IJobHandler;
import com.inno72.job.core.handle.annotation.JobHandler;
import com.inno72.model.Inno72CheckUser;
import com.inno72.service.CheckUserService;

@Component
@JobHandler("Alarm.CheckSmsTask")
public class CheckSmsTask implements IJobHandler {
	@Resource
	private CheckUserService checkUserService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public ReturnT<String> execute(String s) throws Exception {
		logger.info("执行发送未读消息短信定时开始");
		Map<String,Object> map = new HashMap<>();
		List<Inno72CheckUser> checkUserList = checkUserService.selectUnReadByParam(map);
		if(checkUserList != null && checkUserList.size()>0){
			checkUserService.sendSmsToCheck(checkUserList);
		}
		logger.info("执行发送未读消息短信定时结束");
		return new ReturnT<String>(ReturnT.SUCCESS_CODE, "ok");
	}

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}
}
