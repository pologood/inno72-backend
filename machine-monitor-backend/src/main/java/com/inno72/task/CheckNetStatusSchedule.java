package com.inno72.task;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.model.MachineLogInfo;
import com.inno72.plugin.http.HttpClient;
import com.inno72.vo.MachineNetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private MachineMonitorBackendProperties machineMonitorBackendProperties;

    @Scheduled(cron = "0 0/5 * * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
	public void checkNetStatus() {

		log.info("检查并修改网络状态的定时任务，开始执行");
        //查询库中所有数据
        List<MachineLogInfo> list = mongoTpl.find(new Query(), MachineLogInfo.class, "MachineLogInfo");
        if (null != list) {
            List<MachineLogInfo> newMachineNetCloseList = new ArrayList<>();
            List<MachineLogInfo> newMachineNetOpenList = new ArrayList<>();
            for (MachineLogInfo machineLogInfo : list) {
                LocalDateTime createTime = machineLogInfo.getCreateTime();
                Duration duration = Duration.between(createTime, LocalDateTime.now());
                long between = duration.toMinutes();
                if (between >= 10) {
                    newMachineNetCloseList.add(machineLogInfo);

                } else {
                    newMachineNetOpenList.add(machineLogInfo);

                }
            }
            if (newMachineNetCloseList.size() > 0) {
                log.info("网络断开的机器有{}台", newMachineNetCloseList.size());
                List<MachineNetInfo> machineNetInfoList = new ArrayList<>();
                for (MachineLogInfo machineLogInfo : newMachineNetCloseList) {
                    MachineNetInfo machineNetInfo = new MachineNetInfo();
                    machineNetInfo.setMachineCode(machineLogInfo.getMachineId());
                    machineNetInfo.setNetStatus(CommonConstants.NET_CLOSE);
                    machineNetInfoList.add(machineNetInfo);
                }
                String machineNetInfoString = JSONObject.toJSON(machineNetInfoList).toString();
                String urlProp = machineMonitorBackendProperties.getProps().get("updateMachineListNetStatusUrl");
                String result = HttpClient.post(urlProp, machineNetInfoString);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (CommonConstants.RESULT_SUCCESS.equals(jsonObject.getInteger("code"))) {
                    log.info("修改后台网络状态成功");
                }

            }
            if (newMachineNetOpenList.size() > 0) {
                log.info("网络连接中的机器有{}台", newMachineNetOpenList.size());
                List<MachineNetInfo> machineNetInfoList = new ArrayList<>();
                for (MachineLogInfo machineLogInfo : list) {
                    MachineNetInfo machineNetInfo = new MachineNetInfo();
                    machineNetInfo.setMachineCode(machineLogInfo.getMachineId());
                    machineNetInfo.setNetStatus(CommonConstants.NET_OPEN);
                    machineNetInfoList.add(machineNetInfo);
                    String machineNetInfoString = JSONObject.toJSON(machineNetInfoList).toString();
                    String urlProp = machineMonitorBackendProperties.getProps().get("updateMachineListNetStatusUrl");
                    String result = HttpClient.post(urlProp, machineNetInfoString);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (CommonConstants.RESULT_SUCCESS.equals(jsonObject.getInteger("code"))) {
                        log.info("修改后台网络状态成功");
                    }
                }

            }
            log.info("检查并修改网络状态的定时任务，执行结束");

        } else {
            return;
        }
	}
}
