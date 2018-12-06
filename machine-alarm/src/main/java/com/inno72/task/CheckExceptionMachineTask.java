package com.inno72.task;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inno72.job.core.biz.model.ReturnT;
import com.inno72.job.core.handle.IJobHandler;
import com.inno72.job.core.handle.annotation.JobHandler;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.MachineService;

@Component
@JobHandler("Alarm.CheckExceptionMachineTask")
public class CheckExceptionMachineTask implements IJobHandler {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AlarmDetailService alarmDetailService;
	@Override
	public ReturnT<String> execute(String s) throws Exception {
		log.info("获取异常的的需要发送报警机器开始");
		alarmDetailService.addToExceptionMachineBean();
		log.info("获取异常的的需要发送报警机器结束");
		return new ReturnT<String>(ReturnT.SUCCESS_CODE, "ok");
	}

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}
}
