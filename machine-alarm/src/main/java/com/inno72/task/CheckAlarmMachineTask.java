package com.inno72.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.inno72.model.Inno72Machine;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.MachineService;

@Configuration
@EnableScheduling
public class CheckAlarmMachineTask {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MachineService machineService;

	@Resource
	private AlarmDetailService alarmDetailService;

//	@Scheduled(fixedRate = 1000*60*10)
	public void checkAllMachine() {
		log.info("获取全部需要发送报警的机器开始");
		List<Inno72Machine> list = machineService.findAlarmAllMachine();
		log.info("获取全部需要发送报警的机器，共找到" + list.size() + "台机器");
		alarmDetailService.addToMachineBean(list);
		log.info("获取全部需要发送报警的机器结束");
	}

//	@Scheduled(cron = "0 0/1 * * * ?")
	public void checkExceptionMachine() {
		log.info("获取异常的的需要发送报警机器开始");
		alarmDetailService.addToExceptionMachineBean();
		log.info("获取异常的的需要发送报警机器结束");
	}

//	@Scheduled(cron = "0/5 * * * * ?")
	public void sendExceptionMachineAlarm() {
		log.info("发送机器监控异常发送开始");
		alarmDetailService.sendExceptionMachineAlarm();
		log.info("发送机器监控异常发送结束");
	}

//	@Scheduled(fixedRate = 1000*60*60)
	public void updateLastUpdate(){
		log.info("修改最后监控时间开始");
		alarmDetailService.updateMachineStart();
		log.info("修改最后监控时间结束");
	}
}
