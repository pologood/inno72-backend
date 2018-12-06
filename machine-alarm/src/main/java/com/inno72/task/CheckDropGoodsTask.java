package com.inno72.task;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.job.core.biz.model.ReturnT;
import com.inno72.job.core.handle.IJobHandler;
import com.inno72.job.core.handle.annotation.JobHandler;
import com.inno72.model.AlarmDropGoodsBean;
import com.inno72.service.SupplyChannelService;

@Component
@JobHandler("Alarm.CheckDropGoodsTask")
public class CheckDropGoodsTask implements IJobHandler {
	@Resource
	private SupplyChannelService supplyChannelService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public ReturnT<String> execute(String s) throws Exception {
		logger.info("执行掉货异常定时开始");
		List<AlarmDropGoodsBean> list = supplyChannelService.getDropGoodsList();
		if(list != null && list.size()>0){
			supplyChannelService.sendAlarmDropGoods(list);
		}
		logger.info("执行掉货异常定时结束");
		return new ReturnT<String>(ReturnT.SUCCESS_CODE, "ok");
	}

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}
}
