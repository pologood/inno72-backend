package com.inno72.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.inno72.model.AlarmDropGoodsBean;
import com.inno72.service.SupplyChannelService;

@Configuration
@EnableScheduling
public class CheckDropGoodsTask {
	@Resource
	private SupplyChannelService supplyChannelService;


	@Scheduled(cron = "0 0/1 * * * ?")
	public void sendDropGoods(){
		List<AlarmDropGoodsBean> list = supplyChannelService.getDropGoodsList();
		supplyChannelService.sendAlarmDropGoods(list);
	}
}
