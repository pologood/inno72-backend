package com.inno72.task;

import com.inno72.common.CommonConstants;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.model.MachineLogInfo;
import com.inno72.plugin.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.MessageFormat;
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

	@Autowired
	private MachineMonitorBackendProperties machineMonitorBackendProperties;

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
			List<MachineLogInfo> list= mongoTpl.find(query,MachineLogInfo.class,"MachineLogInfo");
			if(null != list){
				for(MachineLogInfo machineLogInfo : list){
					String machineId = machineLogInfo.getMachineId();
					String urlProp = machineMonitorBackendProperties.getProps().get("updateNetStatusUrl");
					log.info("配置文件中获取的url：{}",urlProp);
					String url = MessageFormat.format(urlProp,machineId,CommonConstants.NET_CLOSE);
					log.info("组装后的url：{}",url);
					String result = HttpClient.post(url, "");
					log.info("调用远程服务发送结果是result：{}",result);

				}
				log.info("检查并修改网络状态执行完成");
			}

		}
		log.info("检查并修改网络状态的定时任务执行结束");

	}
}
