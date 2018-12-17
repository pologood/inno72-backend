package com.inno72.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inno72.job.core.biz.model.ReturnT;
import com.inno72.job.core.handle.IJobHandler;
import com.inno72.job.core.handle.annotation.JobHandler;
import com.inno72.model.Inno72Machine;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.MachineService;

@Component
@JobHandler("Alarm.CheckMachineTask")
public class CheckMachineTask implements IJobHandler {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MachineService machineService;

	@Resource
	private AlarmDetailService alarmDetailService;

	@Override
	public ReturnT<String> execute(String s) throws Exception {
		log.info("获取全部需要发送报警的机器开始");
		List<Inno72Machine> list = machineService.findAlarmAllMachine();
		log.info("获取全部需要发送报警的机器，共找到" + list.size() + "台机器");
		alarmDetailService.addToMachineBean(list);
		log.info("获取全部需要发送报警的机器结束");
		return new ReturnT<String>(ReturnT.SUCCESS_CODE, "ok");
	}

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}
}
