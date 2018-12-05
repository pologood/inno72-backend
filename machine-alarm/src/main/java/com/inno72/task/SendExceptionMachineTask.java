package com.inno72.task;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inno72.job.core.biz.model.ReturnT;
import com.inno72.job.core.handle.IJobHandler;
import com.inno72.job.core.handle.annotation.JobHandler;
import com.inno72.service.AlarmDetailService;

@Component
@JobHandler("Alarm.SendExceptionMachineTask")
public class SendExceptionMachineTask implements IJobHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AlarmDetailService alarmDetailService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		logger.info("发送机器监控异常发送开始");
		alarmDetailService.sendExceptionMachineAlarm();
		logger.info("发送机器监控异常发送结束");
		return new ReturnT<String>(ReturnT.SUCCESS_CODE, "ok");
	}

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}
}
