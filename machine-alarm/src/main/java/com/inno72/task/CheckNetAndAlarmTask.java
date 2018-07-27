package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.common.Result;
import com.inno72.common.StringUtil;
import com.inno72.model.*;
import com.inno72.msg.MsgUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.AlarmMsgService;
import com.inno72.service.MachineService;
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

import javax.annotation.Resource;
import java.text.MessageFormat;
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

    @Resource
    private AlarmMsgService alarmMsgService;

    @Resource
    private MachineService machineService;

    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void checkNetStatus() {

        log.info("检查网络状态并预警的定时任务，开始执行");
        //获取当前时间5分钟前的时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime before = localDateTime.minusMinutes(5);
        //查询库
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").lte(before));
        List<MachineLogInfo> list = mongoTpl.find(query, MachineLogInfo.class, "MachineLogInfo");
        if (null != list && list.size() > 0) {
            //查询后台数据库中机器状态是4的机器列表
            Result<List<Inno72Machine>> result = machineService.findMachineByMachineStatus(CommonConstants.MACHINESTATUS_NUMAUL);
            List<Inno72Machine> machines = result.getData();
            log.info("后台数据库中状态是正常的机器列表数据，machines:{}", ((JSONArray) JSONArray.toJSON(machines)).toJSONString());
            if (null != machines && machines.size() > 0) {
                List<Inno72Machine> newList = new ArrayList<>();
                for (MachineLogInfo machineLogInfo : list) {
                    String machineCode = machineLogInfo.getMachineId();
                    for (Inno72Machine inno72Machine : machines) {
                        String machineId = inno72Machine.getMachineCode();
                        String localeStr = inno72Machine.getLocaleStr();
                        if (machineCode.equals(machineId)) {
                            Inno72Machine inno72MachineOne = new Inno72Machine();
                            inno72MachineOne.setMachineCode(machineLogInfo.getMachineId());
                            inno72MachineOne.setCreateTime(machineLogInfo.getCreateTime());
                            inno72MachineOne.setLocaleStr(localeStr);
                            newList.add(inno72MachineOne);
                        }
                    }

                }

                if (null != newList && newList.size() > 0) {
                    for (Inno72Machine machineLogInfo : newList) {
                        LocalDateTime createTime = machineLogInfo.getCreateTime();
                        Duration duration = Duration.between(createTime, LocalDateTime.now());
                        long between = duration.toMinutes();
                        if (between == 5) {
                            //巡检app
                            String code = "push_alarm_common";
                            Map<String, String> params = new HashMap<>();
                            params.put("machineCode", machineLogInfo.getMachineCode());
                            params.put("localStr", machineLogInfo.getLocaleStr());
                            params.put("text", "出现网络连接不上的情况，请及时处理");
                            //查询巡检人员手机号
                            Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                            inno72CheckUserPhone.setMachineCode(machineLogInfo.getMachineCode());
                            String inno72CheckUserPhoneInfo = JSONObject.toJSON(inno72CheckUserPhone).toString();
                            String url1 = machineAlarmProperties.getProps().get("selectPhoneByMachineCode");
                            String res = HttpClient.post(url1, inno72CheckUserPhoneInfo);
                            JSONObject jsonObject2 = JSONObject.parseObject(res);
                            List<Inno72CheckUserPhone> inno72CheckUserPhones = JSON.parseArray(jsonObject2.getString("data"), Inno72CheckUserPhone.class);
                            for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                                String phone = inno72CheckUserPhone1.getPhone();
                                msgUtil.sendPush(code, params, phone, "machineAlarm-CheckNetAndAlarmTask", "【报警】您负责的机器出现网络异常", "");
                            }
                            //保存接口
                            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
                            inno72AlarmMsg.setTitle("报警");
                            inno72AlarmMsg.setType(4);
                            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                            inno72AlarmMsg.setSystem("machineCloseNet");
                            inno72AlarmMsg.setMachineCode(machineLogInfo.getMachineCode());
                            inno72AlarmMsg.setId(StringUtil.getUUID());
                            alarmMsgService.save(inno72AlarmMsg);

                        } else if (between == 8) {
                            //组合报警接口
                            Map<String, String> params = new HashMap<>();
                            params.put("machineCode", machineLogInfo.getMachineCode());
                            params.put("localStr", machineLogInfo.getLocaleStr());
                            params.put("text", "出现网络连接不上的情况，请及时处理");
                            //查询巡检人员手机号
                            Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                            inno72CheckUserPhone.setMachineCode(machineLogInfo.getMachineCode());
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
                            for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                                String phone = inno72CheckUserPhone1.getPhone();
                                String code = "push_alarm_common";
                                msgUtil.sendPush(code, params, phone, "machineAlarm-CheckNetAndAlarmTask", "【报警】您负责的机器出现网络异常", "");
                            }

                            //保存接口
                            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
                            inno72AlarmMsg.setTitle("报警");
                            inno72AlarmMsg.setType(4);
                            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                            inno72AlarmMsg.setSystem("machineCloseNet");
                            inno72AlarmMsg.setMachineCode(machineLogInfo.getMachineCode());
                            inno72AlarmMsg.setId(StringUtil.getUUID());
                            alarmMsgService.save(inno72AlarmMsg);

                        } else if (between > 8 && (between - 8) % 2 == 0) {
                            //钉钉报警接口
                            String code = "dingding_alarm_common";
                            Map<String, String> params = new HashMap<>();
                            params.put("machineCode", machineLogInfo.getMachineCode());
                            params.put("localStr", machineLogInfo.getLocaleStr());
                            params.put("text", "出现网络连接不上的情况，请及时处理");
                            log.info("检查网络状态动态参数是，params：{}", params.toString());
                            msgUtil.sendDDTextByGroup(code, params, groupId, "machineAlarm-CheckNetAndAlarmTask");
                        }
                    }

                } else {
                    return;
                }
            } else {
                return;
            }

        } else {
            return;
        }
        log.info("检查网络状态并预警的定时任务，执行结束");
    }
}







