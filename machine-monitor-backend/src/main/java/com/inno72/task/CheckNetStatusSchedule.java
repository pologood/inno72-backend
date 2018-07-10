package com.inno72.task;

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

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: wxt
 * @Date: 2018/7/6 18:36
 * @Description:检查并修改网络状态的定时任务
 */
@Configuration
@EnableScheduling
public class CheckNetStatusSchedule {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoOperations mongoTpl;

	// 每15分钟执行一次
	@Scheduled(cron = "0 0/15 * * * ?")
	public void checkNetStatus() {

		log.info("检查并修改网络状态的定时任务开始执行");
		//获取当前时间10分钟后的时间
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDateTime after = localDateTime.plusMinutes(10);

		Query query = new Query();
		query.addCriteria(Criteria.where("createTime").lte(after));

		Boolean flag = mongoTpl.exists(query,"MachineLogInfo");

		if(flag == true){
			//调用修改网络状态方法
			MachineLogInfo machineLogInfo = new MachineLogInfo();
			List<MachineLogInfo> list= mongoTpl.find(query,MachineLogInfo.class,"MachineLogInfo");
			log.info("调用修改网络状态的方法");
		}


		log.info("检查并修改网络状态的定时任务执行结束");

	}
}
