package com.inno72.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.common.Result;
import com.inno72.model.MachineLogInfo;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.MachineService;
import com.inno72.vo.MachineNetInfo;

/**
 * @Auther: wxt
 * @Date: 2018/7/6 18:36
 * @Description:检查并修改后台网络状态的定时任务
 */
@Configuration
@EnableScheduling
public class CheckNetStatusSchedule {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private MachineAlarmProperties machineAlarmProperties;

	@Resource
	private MachineService machineService;

	@Scheduled(cron = "0 0/5 * * * ?")
	// @Scheduled(cron = "0/3 * * * * ?")
	public void checkNetStatus() {

		log.info("检查并修改网络状态的定时任务，开始执行");
		// 查询库中所有数据
		List<MachineLogInfo> list = mongoTpl.find(new Query(), MachineLogInfo.class, "MachineLogInfo");
		if (null != list) {
			List<MachineLogInfo> newMachineNetCloseList = new ArrayList<>();
			List<MachineLogInfo> newMachineNetOpenList = new ArrayList<>();
			for (MachineLogInfo machineLogInfo : list) {
				LocalDateTime createTime = machineLogInfo.getCreateTime();
				Duration duration = Duration.between(createTime, LocalDateTime.now());
				long between = duration.toMinutes();
				if (between >= 5) {
					newMachineNetCloseList.add(machineLogInfo);

				} else {
					newMachineNetOpenList.add(machineLogInfo);

				}
			}
			if (newMachineNetCloseList.size() > 0) {
				log.info("网络断开的机器有{}台", newMachineNetCloseList.size());
				// 后台查询网络状态不一致的列表
				Result<List<MachineLogInfo>> result = machineService
						.findMachineNetStatusOpenList(newMachineNetCloseList);
				List<MachineLogInfo> machineList = result.getData();
				log.info("需要修改网络状态的机器有：{}台", machineList.size());
				List<MachineNetInfo> machineNetInfoList = new ArrayList<>();
				for (MachineLogInfo machineLogInfo : machineList) {
					MachineNetInfo machineNetInfo = new MachineNetInfo();
					machineNetInfo.setMachineCode(machineLogInfo.getMachineId());
					machineNetInfo.setNetStatus(CommonConstants.NET_CLOSE);
					machineNetInfoList.add(machineNetInfo);
				}
				if (null != machineNetInfoList && machineNetInfoList.size() > 0) {
					String machineNetInfoString = JSONObject.toJSON(machineNetInfoList).toString();
					log.info("网络状态需要修改的机器列表，machineNetInfoString：{}", machineNetInfoString);
					String urlProp = machineAlarmProperties.getProps().get("updateMachineListNetStatusUrl");
					String res = HttpClient.post(urlProp, machineNetInfoString);
					JSONObject jsonObject = JSONObject.parseObject(res);
					if (CommonConstants.RESULT_SUCCESS.equals(jsonObject.getInteger("code"))) {
						log.info("修改后台网络状态成功");
					}
				}

			}
			/**
			 * if (newMachineNetOpenList.size() > 0) { log.info("网络连接中的机器有{}台",
			 * newMachineNetOpenList.size()); //后台查询网络状态不一致的列表
			 * Result<List<MachineLogInfo>> result =
			 * machineService.findMachineNetStatusCloseList(newMachineNetOpenList);
			 * List<MachineLogInfo> machineList = result.getData();
			 * log.info("需要修改网络状态的机器有：{}台", machineList.size());
			 * List<MachineNetInfo> machineNetInfoList = new ArrayList<>(); for
			 * (MachineLogInfo machineLogInfo : machineList) { MachineNetInfo
			 * machineNetInfo = new MachineNetInfo();
			 * machineNetInfo.setMachineCode(machineLogInfo.getMachineId());
			 * machineNetInfo.setNetStatus(CommonConstants.NET_OPEN);
			 * machineNetInfoList.add(machineNetInfo); } if (null !=
			 * machineNetInfoList && machineNetInfoList.size() > 0) { String
			 * machineNetInfoString =
			 * JSONObject.toJSON(machineNetInfoList).toString();
			 * log.info("需要修改状态的机器列表，machineNetInfoString：{}",
			 * machineNetInfoString); String urlProp =
			 * machineAlarmProperties.getProps().get("updateMachineListNetStatusUrl");
			 * String res = HttpClient.post(urlProp, machineNetInfoString);
			 * JSONObject jsonObject = JSONObject.parseObject(res); if
			 * (CommonConstants.RESULT_SUCCESS.equals(jsonObject.getInteger("code")))
			 * { log.info("修改后台网络状态成功"); } }
			 * 
			 * 
			 * }
			 **/
			log.info("检查并修改网络状态的定时任务，执行结束");

		} else {
			return;
		}
	}
}
