package com.inno72.task;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.StringUtil;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.redis.IRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SupplyChannelTask {

    @Resource
    private SupplyChannelService supplyChannelService;

    @Resource
    private IRedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Scheduled(cron = "0/10 * * * * ?")
    public void typeOneTask(){
        logger.info("定时获取货道商品数量小于10%的货道开始。。。。。。。");
        pushToAlarm(1);
        logger.info("定时获取货道商品数量小于10%的货道结束。。。。。。。");
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    public void typeTwoTask(){
        logger.info("定时获取货道商品数量小于20%的货道开始。。。。。。。");
        pushToAlarm(2);
        logger.info("定时获取货道商品数量小于20%的货道结束。。。。。。。");
    }


    public void pushToAlarm(int lackGoodsType){
        logger.info("触发定时获取货道缺货类型"+lackGoodsType+"的货道");
        List<Inno72SupplyChannel> list = supplyChannelService.findByTaskParam(lackGoodsType);
        if(list != null && list.size()>0){
            AlarmMessageBean alarmMessageBean = new AlarmMessageBean();
            alarmMessageBean.setSystem("machineLackGoods");
            alarmMessageBean.setType("machineLackGoodsException");
            for(Inno72SupplyChannel supplyChannel:list){
                String id = supplyChannel.getId();
                String key = "LACK_GOODS_TYPE_"+lackGoodsType+"_"+id;
//                redisUtil.del(key);
                String value = redisUtil.get(key);
                if(StringUtil.isEmpty(value)){
                    ChannelGoodsAlarmBean alarmBean = new ChannelGoodsAlarmBean();
                    alarmBean.setChannelNum(supplyChannel.getCode());
                    alarmBean.setGoodsName(supplyChannel.getGoodsName());
                    alarmBean.setLackGoodsType(lackGoodsType);
                    alarmBean.setMachineCode(supplyChannel.getMachineCode());
                    alarmMessageBean.setData(alarmBean);
                    logger.info("货道缺货发送push{}",JSONObject.toJSONString(alarmMessageBean));
                    redisUtil.publish("moniterAlarm",JSONObject.toJSONString(alarmMessageBean));
                    redisUtil.setex(key,60*60*2,"1");

                }
            }
        }
    }
}
