package com.inno72.task;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.model.MachineLogInfo;
import com.inno72.model.NetOffMachineInfo;
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

	@Scheduled(cron = "0 0/15 * * * ?")
	public void checkNetStatus() {

		log.info("检查并修改网络状态的定时任务，开始执行");
		//获取当前时间10分钟后的时间
		LocalDateTime localDateTime = LocalDateTime.now();
		LocalDateTime after = localDateTime.plusMinutes(10);
		//查询库
		Query query = new Query();
		query.addCriteria(Criteria.where("createTime").lte(after));
		List<MachineLogInfo> list= mongoTpl.find(query,MachineLogInfo.class,"MachineLogInfo");
		log.info("网络断开的机器有{}台",list.size());
		if(null != list){
			for(MachineLogInfo machineLogInfo : list){
				String machineId = machineLogInfo.getMachineId();
				//组装url
				String urlProp = machineMonitorBackendProperties.getProps().get("updateNetStatusUrl");
				String url = MessageFormat.format(urlProp,machineId,CommonConstants.NET_CLOSE);
				//调用修改网络状态方法
				String result = HttpClient.post(url, "");
				JSONObject jsonObject = JSONObject.parseObject(result);
				Integer resultCdoe = jsonObject.getInteger("code");
				//维护一个断网机器信息表
				if(CommonConstants.RESULT_SUCCESS.equals(resultCdoe)){
					NetOffMachineInfo netOffMachineInfo = new NetOffMachineInfo();
					netOffMachineInfo.setMachineId(machineId);
					mongoTpl.save(netOffMachineInfo,"NetOffMachineInfo");
					}

				}
			}

		log.info("检查并修改网络状态的定时任务，执行结束");

	}
}
