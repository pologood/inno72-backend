package com.inno72.task;

import com.inno72.common.CommonConstants;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.model.MachineLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wxt
 * @Date: 2018/7/17 19:53
 * @Description:检查网络状态并预警
 */
@Configuration
@EnableScheduling
public class CheckNetAndAlarmTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoOperations mongoTpl;

    //@Scheduled(cron = "0 0/1 * * * ?")
    public void checkNetStatus() {

        log.info("检查网络状态并预警的定时任务，开始执行");
        //获取当前时间5分钟前的时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime before = localDateTime.minusMinutes(5);
        //查询库
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").lte(before));
        List<MachineLogInfo> list = mongoTpl.find(query, MachineLogInfo.class, "MachineLogInfo");
        if (null != list) {
            //TODO   调用报警
            List<MachineLocaleInfo> fileAlarm = new ArrayList<>();
            List<MachineLocaleInfo> eightAlarm = new ArrayList<>();
            List<MachineLocaleInfo> bigEightAlarm = new ArrayList<>();
            for (MachineLogInfo machineLogInfo : list) {
                LocalDateTime createTime = machineLogInfo.getCreateTime();
                Duration duration = Duration.between(LocalDateTime.now(), createTime);
                long between = duration.toMinutes();

                if (CommonConstants.BETWEEN_FIVE == between) {
                    MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
                    machineLocaleInfo.setMachineCode(machineLogInfo.getMachineId());
                    fileAlarm.add(machineLocaleInfo);
                    //调用查询点位信息的接口
                } else if (CommonConstants.BETWEEN_EIGHT == between) {
                    MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
                    machineLocaleInfo.setMachineCode(machineLogInfo.getMachineId());
                    eightAlarm.add(machineLocaleInfo);
                } else if (CommonConstants.BETWEEN_EIGHT < between && (between - CommonConstants.BETWEEN_EIGHT) % 2 == 0) {
                    MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
                    machineLocaleInfo.setMachineCode(machineLogInfo.getMachineId());
                    bigEightAlarm.add(machineLocaleInfo);
                }
            }
            if (fileAlarm.size() > 0) {
                //发巡检app报警
            }
            if (eightAlarm.size() > 0) {
                //发短信和巡检app报警
            }
            if (bigEightAlarm.size() > 0) {
                //发钉钉报警
            }

            log.info("检查网络状态并预警的定时任务，执行结束");
        } else {
            return;
        }

    }


}




