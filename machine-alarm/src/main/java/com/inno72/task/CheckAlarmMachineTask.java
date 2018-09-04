package com.inno72.task;

import com.inno72.model.AlarmMachineBean;
import com.inno72.model.Inno72Machine;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class CheckAlarmMachineTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MachineService machineService;

    @Resource
    private AlarmDetailService alarmDetailService;

    @Autowired
    private MongoOperations mongoTpl;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkAllMachine(){
        log.info("获取全部需要发送报警的机器开始");
        List<Inno72Machine> list = machineService.findAlarmAllMachine();
        if(list != null && list.size()>0){
            log.info("获取全部需要发送报警的机器，共找到"+list.size()+"台机器");
            alarmDetailService.addToMachineBean(list);
        }else {
            log.info("无新增机器到报警系统");
        }
        log.info("获取全部需要发送报警的机器结束");
    }

//    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkExceptionMachine(){
        log.info("获取异常的的需要发送报警机器开始");
        alarmDetailService.addToExceptionMachineBean();
        log.info("获取异常的的需要发送报警机器结束");
    }


//    @Scheduled(cron = "0/5 * * * * ?")
    public void sendExceptionMachineAlarm(){
        log.info("发送机器监控异常发送开始");
        alarmDetailService.sendExceptionMachineAlarm();
        log.info("发送机器监控异常发送结束");
    }
}
