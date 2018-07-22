package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.model.MachineLogInfo;
import com.inno72.msg.MsgUtil;
import com.inno72.plugin.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wxt
 * @Date: 2018/7/17 19:53
 * @Description:检查网络状态并预警
 */
@Configuration
@EnableScheduling
public class CheckNetAndAlarmTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoOperations mongoTpl;

    @Autowired
    private MachineAlarmProperties machineAlarmProperties;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    @Autowired
    private MsgUtil msgUtil;

    @Scheduled(cron = "0 0/1 * * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void checkNetStatus() {

        log.info("检查网络状态并预警的定时任务，开始执行");
        //获取当前时间5分钟前的时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime before = localDateTime.minusMinutes(5);
        //查询库
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").lte(before));
        List<MachineLogInfo> list = mongoTpl.find(query, MachineLogInfo.class, "MachineLogInfo");
        if (null != list) {
            for (MachineLogInfo machineLogInfo : list) {
                //根据机器编码查询点位接口
                List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
                MachineLocaleInfo machineLocale = new MachineLocaleInfo();
                machineLocale.setMachineCode(machineLogInfo.getMachineId());
                machineLocaleInfos.add(machineLocale);
                String machineLocaleInfoString = JSONObject.toJSON(machineLocaleInfos).toString();
                String url = machineAlarmProperties.getProps().get("findLocalByMachineCode");
                String returnMsg = HttpClient.post(url, machineLocaleInfoString);
                JSONObject jsonObject1 = JSONObject.parseObject(returnMsg);
                List<MachineLocaleInfo> MachineLocaleInfos = JSON.parseArray(jsonObject1.getString("data"), MachineLocaleInfo.class);
                String localStr = "";
                for (MachineLocaleInfo machineLocaleInfo : MachineLocaleInfos) {
                    localStr = machineLocaleInfo.getLocaleStr();
                }

                LocalDateTime createTime = machineLogInfo.getCreateTime();
                Duration duration = Duration.between(createTime, LocalDateTime.now());
                long between = duration.toMinutes();
                if (between == 5) {
                    //巡检app
                    String code = "push_alarm_common";
                    Map<String, String> params = new HashMap<>();
                    params.put("machineCode", machineLogInfo.getMachineId());
                    params.put("localStr", localStr);
                    params.put("text", "出现网络连接不上的情况，请及时处理");
                    msgUtil.sendPush(code, params, machineLogInfo.getMachineId(), "machineAlarm-CheckNetAndAlarmTask", "【报警】您负责的机器出现网络异常", "");
                } else if (between == 8) {
                    //组合报警接口
                    Map<String, String> params = new HashMap<>();
                    params.put("machineCode", machineLogInfo.getMachineId());
                    params.put("localStr", localStr);
                    params.put("text", "出现网络连接不上的情况，请及时处理");
                    //查询巡检人员手机号
                    Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                    inno72CheckUserPhone.setMachineCode(machineLogInfo.getMachineId());
                    String inno72CheckUserPhoneInfo = JSONObject.toJSON(inno72CheckUserPhone).toString();
                    String url1 = machineAlarmProperties.getProps().get("selectPhoneByMachineCode");
                    String res = HttpClient.post(url1, inno72CheckUserPhoneInfo);
                    JSONObject jsonObject2 = JSONObject.parseObject(res);
                    List<Inno72CheckUserPhone> inno72CheckUserPhones = JSON.parseArray(jsonObject2.getString("data"), Inno72CheckUserPhone.class);
                    for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                        String code = "sms_alarm_common";
                        String phone = inno72CheckUserPhone1.getPhone();
                        msgUtil.sendSMS(code, params, phone, "machineAlarm-CheckNetAndAlarmTask");
                    }
                    String code = "push_alarm_common";
                    msgUtil.sendPush(code, params, machineLogInfo.getMachineId(), "machineAlarm-CheckNetAndAlarmTask", "【报警】您负责的机器出现网络异常", "");

                } else if (between > 8 && (between - 8) % 2 == 0) {
                    //钉钉报警接口
                    String code = "push_dingding_checkNetClose";
                    Map<String, String> params = new HashMap<>();
                    params.put("machineCode", machineLogInfo.getMachineId());
                    params.put("localStr", localStr);
                    params.put("text", "出现网络连接不上的情况，请及时处理");
                    msgUtil.sendDDTextByGroup(code, params, groupId, "machineAlarm-CheckNetAndAlarmTask");
                }
            }

            log.info("检查网络状态并预警的定时任务，执行结束");
        } else {
            return;
        }

    }


}




