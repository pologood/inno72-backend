package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.model.MachineLogInfo;
import com.inno72.model.NetOffMachineInfo;
import com.inno72.plugin.http.HttpClient;
import com.inno72.vo.MachineNetInfo;
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
import java.util.ArrayList;
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
		LocalDateTime before = localDateTime.minusMinutes(10);
		//查询库
		Query query = new Query();
		query.addCriteria(Criteria.where("createTime").lte(before));
		List<MachineLogInfo> list= mongoTpl.find(query,MachineLogInfo.class,"MachineLogInfo");
		log.info("网络断开的机器有{}台",list.size());
		if(null != list){
			List<MachineNetInfo> machineNetInfoList = new ArrayList<>();
			for(MachineLogInfo machineLogInfo : list){
				MachineNetInfo machineNetInfo = new MachineNetInfo();
				machineNetInfo.setMachineCode(machineLogInfo.getMachineId());
				machineNetInfo.setNetStatus(CommonConstants.NET_CLOSE);
				machineNetInfoList.add(machineNetInfo);
			}
            String machineNetInfoString = JSONObject.toJSON(machineNetInfoList).toString();
			String urlProp = machineMonitorBackendProperties.getProps().get("updateMachineListNetStatusUrl");
			String result = HttpClient.post(urlProp, machineNetInfoString);
			JSONObject jsonObject = JSONObject.parseObject(result);
			Integer resultCdoe = jsonObject.getInteger("code");
			List<MachineNetInfo> machineIdListInfo = JSON.parseArray(jsonObject.getString("data"), MachineNetInfo.class);
			//List<MachineNetInfo> machineIdListInfo = (List<MachineNetInfo>) jsonObject.get("data");
			//维护一个断网机器信息表
			if(CommonConstants.RESULT_SUCCESS.equals(resultCdoe) && machineIdListInfo != null){
				for(MachineNetInfo machineNetInfoOne : machineIdListInfo){
					NetOffMachineInfo netOffMachineInfo = new NetOffMachineInfo();
					netOffMachineInfo.setMachineId(machineNetInfoOne.getMachineCode());
					mongoTpl.save(netOffMachineInfo,"NetOffMachineInfo");
				}
			}

		}

		log.info("检查并修改网络状态的定时任务，执行结束");

	}
}
