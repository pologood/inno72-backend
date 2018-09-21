package com.inno72.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSON;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.plugin.http.HttpClient;

@Configuration
@EnableScheduling
public class CheckSupplyLackGoodsTask {
    @Autowired
    private MachineAlarmProperties machineAlarmProperties;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    Map<String,Object> map = new HashMap<>();
//    @Scheduled(cron = "0 0/20 * * * ?")
    public void typeOneTask(){
        String url = machineAlarmProperties.getProps().get("findAndPushByTaskParam");
        logger.info("定时获取货道商品缺货的机器开始。。。。。。。");
        String data = JSON.toJSONString(map);
        HttpClient.post(url, data);
        logger.info("定时获取货道商品缺货的机器结束。。。。。。。");
    }

}
