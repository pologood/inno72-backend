package com.inno72.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSON;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.model.AlarmLackGoodsBean;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.SupplyChannelService;

@Configuration
@EnableScheduling
public class CheckSupplyLackGoodsTask {
	@Resource
    private SupplyChannelService supplyChannelService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    Map<String,Object> map = new HashMap<>();
	@Scheduled(cron = "0/5 * * * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void lackGoods(){
		List<AlarmLackGoodsBean> list = supplyChannelService.getLackGoodsList();
		if(list != null && list.size()>0){
			supplyChannelService.sendAlarmLackGoods(list);
		}

    }

}
