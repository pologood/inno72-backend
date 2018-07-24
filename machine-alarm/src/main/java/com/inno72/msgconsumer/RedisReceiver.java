package com.inno72.msgconsumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineAlarmProperties;
import com.inno72.common.StringUtil;
import com.inno72.model.*;
import com.inno72.msg.MsgUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.AlarmMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxt
 * @date 2018/07/19
 */
@Service
public class RedisReceiver {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AlarmMsgService alarmMsgService;

    @Autowired
    private MongoOperations mongoTpl;

    @Autowired
    private MsgUtil msgUtil;

    @Autowired
    private MachineAlarmProperties machineAlarmProperties;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    public void receiveMessage(String message) throws UnsupportedEncodingException {


        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        String system = jsonObject.getString("system");
        log.info("当前接收的消息来自于系统：{}，类型：{}", system, type);

        if ((CommonConstants.SYS_MACHINE_CHANNEL).equals(system)) {
            //接收货道信息并转数据类型
            AlarmMessageBean<MachineStatus> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineStatus>>() {
                    });
            MachineStatus machineStatus = alarmMessageBean.getData();
            String goodsChannelStatus = machineStatus.getGoodsChannelStatus();
            String machineId = machineStatus.getMachineId();
            log.info("机器货道异常，machineCode:{}", machineId);
            //根据机器编码查询点位信息
            List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
            MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
            machineLocaleInfo.setMachineCode(machineId);
            machineLocaleInfos.add(machineLocaleInfo);
            String machineLocaleInfoString = JSONObject.toJSON(machineLocaleInfos).toString();
            String url = machineAlarmProperties.getProps().get("findLocalByMachineCode");
            String returnMsg = HttpClient.post(url, machineLocaleInfoString);
            JSONObject jsonObject1 = JSONObject.parseObject(returnMsg);
            List<MachineLocaleInfo> MachineLocaleInfos = JSON.parseArray(jsonObject1.getString("data"), MachineLocaleInfo.class);

            String localStr = "";
            for (MachineLocaleInfo machineLocale : MachineLocaleInfos) {
                localStr = machineLocale.getLocaleStr();
            }
            //调用接口查询故障信息
            List<GoodsChannelBean> goodsChannelBean = JSON.parseArray(goodsChannelStatus, GoodsChannelBean.class);
            String machineNetInfoString = JSONObject.toJSON(goodsChannelBean).toString();
            String urlProp = machineAlarmProperties.getProps().get("findChannelError");
            String result = HttpClient.post(urlProp, machineNetInfoString);
            JSONObject json = JSONObject.parseObject(result);
            List<GoodsChannelBean> goodsChannelBeans = JSON.parseArray(json.getString("data"), GoodsChannelBean.class);

            //获取货道号与故障描述
            for (GoodsChannelBean goodsChannel : goodsChannelBeans) {
                int channelNum = goodsChannel.getGoodsChannelNum();
                String describtion = goodsChannel.getDescription();
                String code = "sms_alarm_common";
                Map<String, String> params = new HashMap<>();
                params.put("machineCode", machineId);
                params.put("localStr", localStr);
                params.put("text", "出现掉货异常，货道编号是：" + channelNum + "故障原因是" + describtion);
                log.info("货道故障发送短讯，参数param：{}", params.toString());
                //根据机器编码查询对应巡检人员
                Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                inno72CheckUserPhone.setMachineCode(machineId);
                String inno72CheckUserPhoneInfo = JSONObject.toJSON(inno72CheckUserPhone).toString();
                String url1 = machineAlarmProperties.getProps().get("selectPhoneByMachineCode");
                String res = HttpClient.post(url1, inno72CheckUserPhoneInfo);
                JSONObject jsonObject2 = JSONObject.parseObject(res);
                List<Inno72CheckUserPhone> inno72CheckUserPhones = JSON.parseArray(jsonObject2.getString("data"), Inno72CheckUserPhone.class);
                for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                    String phone = inno72CheckUserPhone1.getPhone();
                    msgUtil.sendSMS(code, params, phone, "machineAlarm-RedisReceiver");
                }

            }

            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_CHANNEL_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("货道故障");
                inno72AlarmMsg.setMachineCode(machineId);
                inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                inno72AlarmMsg.setSystem(system);
                inno72AlarmMsg.setId(StringUtil.getUUID());
                alarmMsgService.save(inno72AlarmMsg);
            }


        } else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<MachineDropGoodsBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineDropGoodsBean>>() {
                    });
            MachineDropGoodsBean machineDropGoods = alarmMessageBean.getData();
            String machineCode = machineDropGoods.getMachineCode();
            String channelNum = machineDropGoods.getChannelNum();
            String describtion = machineDropGoods.getDescribtion();
            //根据机器编码查询点位接口
            List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
            MachineLocaleInfo machineLocale = new MachineLocaleInfo();
            machineLocale.setMachineCode(machineCode);
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
            //保存消息次数等信息
            Query query = new Query();
            query.addCriteria(Criteria.where("machineCode").is(machineCode));
            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class);
            if (dropGoodsExceptionInfoList.size() > 0) {
                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
                    dropGoodsExceptionInfo.setErrorNum(updateNum);
                    //连续掉货两次
                    if (updateNum == 2) {
                        //巡检app接口
                        String code = "push_alarm_common";
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", "出现掉货异常，请及时处理");
                        msgUtil.sendPush(code, params, "machineCode", "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                    } else if (updateNum == 5) {
                        //组合报警接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", channelNum + describtion + "出现掉货异常，请及时处理");
                        //查询巡检人员手机号
                        Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                        inno72CheckUserPhone.setMachineCode(machineCode);
                        String inno72CheckUserPhoneInfo = JSONObject.toJSON(inno72CheckUserPhone).toString();
                        String url1 = machineAlarmProperties.getProps().get("selectPhoneByMachineCode");
                        String res = HttpClient.post(url1, inno72CheckUserPhoneInfo);
                        JSONObject jsonObject2 = JSONObject.parseObject(res);
                        List<Inno72CheckUserPhone> inno72CheckUserPhones = JSON.parseArray(jsonObject2.getString("data"), Inno72CheckUserPhone.class);
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            String phone = inno72CheckUserPhone1.getPhone();
                            String code = "sms_alarm_common";
                            msgUtil.sendSMS(code, params, phone, "machineAlarm-RedisReceiver");
                        }
                        String code = "push_alarm_common";
                        msgUtil.sendPush(code, params, "machineCode", "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
                        //钉钉报警接口
                        String code = "dingding_alarm_common";
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", channelNum + describtion + "出现掉货异常，请及时处理");
                        msgUtil.sendDDTextByGroup(code, params, groupId, "machineAlarm-RedisReceiver");

                    }
                }
            } else {
                DropGoodsExceptionInfo dropGoodsExceptionInfo = new DropGoodsExceptionInfo();
                dropGoodsExceptionInfo.setCreateTime(LocalDateTime.now());
                dropGoodsExceptionInfo.setErrorNum(1);
                dropGoodsExceptionInfo.setMachineCode(machineCode);
                mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
            }

            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("掉货异常报警");
                inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                inno72AlarmMsg.setSystem(system);
                inno72AlarmMsg.setMachineCode(machineCode);
                inno72AlarmMsg.setId(StringUtil.getUUID());
                alarmMsgService.save(inno72AlarmMsg);
            }

        } else if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
                    });
            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
            log.info("商品缺货消息，machineCode:{}", channelGoodsAlarmBean.getMachineCode());

            //根据机器编码查询点位接口
            List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
            MachineLocaleInfo machineLocale = new MachineLocaleInfo();
            machineLocale.setMachineCode(channelGoodsAlarmBean.getMachineCode());
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
            //调用报警接口
            //缺货20%或者缺货10%
            if (CommonConstants.LACKGOODS_TENPERCENT == channelGoodsAlarmBean.getLackGoodsType()) {
                //组合报警接口
                Map<String, String> params = new HashMap<>();
                params.put("machineCode", channelGoodsAlarmBean.getMachineCode());
                params.put("localStr", localStr);
                params.put("text", "缺货10%，请及时处理");
                //查询巡检人员手机号
                Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                inno72CheckUserPhone.setMachineCode(channelGoodsAlarmBean.getMachineCode());
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
                msgUtil.sendPush(code, params, channelGoodsAlarmBean.getMachineCode(), "machineAlarm-CheckNetAndAlarmTask", "【缺货】您负责的机器需要补货", "");
            } else {
                //组合报警接口
                Map<String, String> params = new HashMap<>();
                params.put("machineCode", channelGoodsAlarmBean.getMachineCode());
                params.put("localStr", localStr);
                params.put("text", "缺货20%，请及时处理");
                //查询巡检人员手机号
                Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
                inno72CheckUserPhone.setMachineCode(channelGoodsAlarmBean.getMachineCode());
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
                msgUtil.sendPush(code, params, channelGoodsAlarmBean.getMachineCode(), "machineAlarm-CheckNetAndAlarmTask", "【缺货】您负责的机器需要补货", "");
            }
            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_LACKGOODS_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("商品缺货异常报警");
                inno72AlarmMsg.setMachineCode(channelGoodsAlarmBean.getMachineCode());
                inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                inno72AlarmMsg.setSystem(system);
                inno72AlarmMsg.setId(StringUtil.getUUID());
                alarmMsgService.save(inno72AlarmMsg);
            }

        } else {
            return;
        }

    }
}
