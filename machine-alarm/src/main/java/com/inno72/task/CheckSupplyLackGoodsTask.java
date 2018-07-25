package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.plugin.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class CheckSupplyLackGoodsTask {
    @Autowired
    private MachineAlarmProperties machineAlarmProperties;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    Map<String,Object> map = new HashMap<>();
    @Scheduled(cron = "0 5 * * * ?")
    public void typeOneTask(){
        String url = machineAlarmProperties.getProps().get("findAndPushByTaskParam");
        logger.info("定时获取货道商品数量小于10%的货道开始。。。。。。。");
        map.put("lackGoodsType",1);
        String data = JSON.toJSONString(map);
        HttpClient.post(url, data);
        logger.info("定时获取货道商品数量小于10%的货道结束。。。。。。。");
    }

    @Scheduled(cron = "0 5 * * * * ?")
    public void typeTwoTask(){
        logger.info("定时获取货道商品数量小于20%的货道开始。。。。。。。");
        String url = machineAlarmProperties.getProps().get("findAndPushByTaskParam");
        map.put("lackGoodsType",2);
        String data = JSON.toJSONString(map);
        HttpClient.post(url, data);
        logger.info("定时获取货道商品数量小于20%的货道结束。。。。。。。");
    }
}
