package com.inno72.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Auther: wxt
 * @Date: 2018/7/16 17:00
 * @Description:检测机器上是否有活动
 */
@Configuration
@EnableScheduling
public class CheckActivityIsExist {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkActivityIsExist() {

        log.info("检查机器上是否有活动排期的定时任务，开始执行");


        log.info("检查机器上是否有活动排期的定时任务，执行结束");

    }

}

