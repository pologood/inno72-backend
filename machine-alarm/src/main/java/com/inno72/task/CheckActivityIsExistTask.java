package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.DateUtil;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.msg.MsgUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.vo.Inno72NoPlanInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inno72.common.DateUtil.DF_FULL_S1;

/**
 * @Auther: wxt
 * @Date: 2018/7/16 17:00
 * @Description:检测机器上是否有活动
 */
@Configuration
@EnableScheduling
public class CheckActivityIsExistTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MachineAlarmProperties machineAlarmProperties;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    @Autowired
    private MsgUtil msgUtil;

    @Scheduled(cron = "0 0 10 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void checkActivityIsExist() {

        log.info("检查机器上是否有活动排期的定时任务，开始执行");

        String nowTime = DateUtil.toTimeStr(LocalDateTime.now(), DF_FULL_S1);
        //查询无活动机器列表
        String urlProp = machineAlarmProperties.getProps().get("noPlanMachine");
        String url = MessageFormat.format(urlProp, nowTime);
        String noPlanInfoVos = HttpClient.post(url, "");
        JSONObject jsonObject = JSONObject.parseObject(noPlanInfoVos);
        //Integer resultCdoe = jsonObject.getInteger("code");
        List<Inno72NoPlanInfoVo> noPlanInfoVoList = JSON.parseArray(jsonObject.getString("data"), Inno72NoPlanInfoVo.class);
        log.info("检查机器上是否有活动排期的定时任务，查询后list：{}", noPlanInfoVoList.toString());
        //调用发送短信接口
        if (null != noPlanInfoVoList) {
            for (Inno72NoPlanInfoVo inno72NoPlanInfoVo : noPlanInfoVoList) {
                String machineCode = inno72NoPlanInfoVo.getMachineCode();
                //根据machineCode查询机器点位
                //查询点位接口
                List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
                MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
                machineLocaleInfo.setMachineCode(machineCode);
                machineLocaleInfos.add(machineLocaleInfo);
                String machineLocaleInfoString = JSONObject.toJSON(machineLocaleInfos).toString();
                String findLocalUrl = machineAlarmProperties.getProps().get("findLocalByMachineCode");
                String returnMsg = HttpClient.post(findLocalUrl, machineLocaleInfoString);
                JSONObject jsonObject1 = JSONObject.parseObject(returnMsg);
                List<MachineLocaleInfo> machineLocaleInfoList = JSON.parseArray(jsonObject1.getString("data"), MachineLocaleInfo.class);
                String localStr = "";
                for (MachineLocaleInfo machineLocale : machineLocaleInfoList) {
                    localStr = machineLocale.getLocaleStr();
                }
                String code = "dingding_alarm_common";
                Map<String, String> params = new HashMap<>();
                params.put("machineCode", machineCode);
                params.put("localStr", localStr);
                params.put("text", "在当前时间没有进行的活动，请及时安排活动");
                msgUtil.sendDDTextByGroup(code, params, groupId, "machineAlarm-CheckActivityIsExistTask");

            }

        } else {
            return;
        }

        log.info("检查机器上是否有活动排期的定时任务，执行结束");

    }
}

