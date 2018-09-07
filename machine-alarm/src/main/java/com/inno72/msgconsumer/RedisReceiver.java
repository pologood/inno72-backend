package com.inno72.msgconsumer;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.model.DropGoodsExceptionInfo;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.model.MachineDropGoodsBean;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.msg.MsgUtil;
import com.inno72.service.AlarmMsgService;
import com.inno72.service.CheckUserService;
import com.inno72.service.LocaleService;
import com.inno72.service.MachineService;
import com.inno72.service.SupplyChannelService;

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

    @Resource
    private LocaleService localeService;

    @Resource
    private CheckUserService checkUserService;

    @Resource
    private MachineService machineService;

    @Resource
    private SupplyChannelService supplyChannelService;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    public void receiveMessage(String message) throws UnsupportedEncodingException {


        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        String system = jsonObject.getString("system");
        log.info("receive msg:{}", message);
		String text = "";
		String active = System.getenv("spring_profiles_active");


        if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
                    });
            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
            int surPlusNum = channelGoodsAlarmBean.getSurPlusNum();
            List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(channelGoodsAlarmBean.getMachineCode());
            Map<String, String> param = new HashMap<>();
            String localStr = channelGoodsAlarmBean.getLocaleStr();
            String machineCode = channelGoodsAlarmBean.getMachineCode();
            String goodsName = channelGoodsAlarmBean.getGoodsName();
            param.put("machineCode", channelGoodsAlarmBean.getMachineCode());
            param.put("localStr", localStr);

            if(surPlusNum == 10){
//                if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
//                    text = "您好，【互动管家】您负责的机器，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于10个，请及时补货";
//                    param.put("text",  text);
//                    for(Inno72CheckUserPhone userPhone :inno72CheckUserPhones){
//                        msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
//                    }
//                }
				text = goodsName + "数量已少于10个，请及时处理。";
				param.put("text",  text);
				for(Inno72CheckUserPhone userPhone :inno72CheckUserPhones){
					msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
				}
                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于10个，请及时补货";
                param.put("text",StringUtil.setText(text,active));
                msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
            }else if(surPlusNum == 5){
//                if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
//                    text = "您好，【互动管家】您负责的机器，" + localStr + "，机器编号：" + machineCode + "，" + goodsName + "数量已少于5个，请及时补货";
//                    param.put("text", text);
//                    for (Inno72CheckUserPhone userPhone : inno72CheckUserPhones) {
//                        msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
//                    }
//                }
				text = goodsName + "数量已少于5个，请及时处理。";
				param.put("text", text);
				for (Inno72CheckUserPhone userPhone : inno72CheckUserPhones) {
					msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
				}
                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于5个，请及时补货";
				param.put("text",StringUtil.setText(text,active));
                msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
            }else if(surPlusNum<5){
                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于5个，请及时补货";
				param.put("text",StringUtil.setText(text,active));
                msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
            }
        }else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<MachineDropGoodsBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineDropGoodsBean>>() {
                    });
            MachineDropGoodsBean machineDropGoods = alarmMessageBean.getData();
            String machineCode = machineDropGoods.getMachineCode();
            String channelNum = machineDropGoods.getChannelNum();
            log.info("machineDropGoods send msg ，machineCode：{}，channelNum：{}，describtion：{}", machineCode, channelNum, machineDropGoods.getDescribtion());
            //保存消息次数等信息
            Query query = new Query();
            query.addCriteria(Criteria.where("machineCode").is(machineCode));
            log.info("machineDropGoods send msg ，machineCode：{}", machineCode);
            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class, "DropGoodsExceptionInfo");
            if (dropGoodsExceptionInfoList.size() > 0) {
                //根据机器编码查询点位接口
                String localStr = getLocaleString(machineCode);
                log.info("machineDropGoods send msg ，localStr:{}", localStr);
                //循环
                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
                    log.info("machineDropGoods send msg ，save to mongo machineCode : {},num：{}", dropGoodsExceptionInfo.getMachineCode(), updateNum);
                    dropGoodsExceptionInfo.setErrorNum(updateNum);
                    mongoTpl.remove(query, "DropGoodsExceptionInfo");
                    mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
                    //连续掉货两次
                    if (updateNum == 2) {
                        //巡检app接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。");
                        log.info("machineDropGoods send msg ，params：{}", params.toString());
                        //查询巡检人员手机号
                        List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            String phone = inno72CheckUserPhone1.getPhone();
                            msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                        }

                        //保存接口
                        int lackNum = 0;
						alarmMsgService.saveAlarmMsg(type, system, machineCode, lackNum, localStr);

                    } else if (updateNum == 5) {
                        //巡检app接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。");
                        log.info("machineDropGoods send msg ，params：{}", params.toString());
                        //查询巡检人员手机号
                        List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);

                        //企业微信提醒
                        List<String> userIdList = new ArrayList<>();
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            userIdList.add(inno72CheckUserPhone1.getUserId());
                        }
                        String userIdString = StringUtils.join(userIdList.toArray(), "|");
                        log.info("userIdString:{}", userIdString);
                        Map<String, String> m = new HashMap<>();
                        m.put("touser", userIdString);
                        m.put("agentid", "1000002");
                        msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, "machineAlarm-RedisReceiver");

                        //巡检提醒
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            String phone = inno72CheckUserPhone1.getPhone();
                            msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                        }
                        //保存接口
                        int lackNum = 0;
						alarmMsgService.saveAlarmMsg(type, system, machineCode, lackNum, localStr);

                        //钉钉报警
                        Map<String, String> param = new HashMap<>();
                        param.put("machineCode", machineCode);
                        param.put("localStr", localStr);
                        text = "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。";
						param.put("text",StringUtil.setText(text,active));
                        msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");

                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
                        //钉钉报警接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        text = "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。";
						params.put("text",StringUtil.setText(text,active));
                        msgUtil.sendDDTextByGroup("dingding_alarm_common", params, groupId, "machineAlarm-RedisReceiver");

                    }
                    Inno72Machine machine = machineService.findByCode(machineCode);
                    Inno72SupplyChannel supplyChannel = new Inno72SupplyChannel();
                    supplyChannel.setCode(channelNum);
                    supplyChannel.setIsDelete(1);
                    supplyChannel.setMachineId(machine.getId());
                    supplyChannelService.closeSupply(supplyChannel);
                }
            } else {
                DropGoodsExceptionInfo dropGoodsExceptionInfo = new DropGoodsExceptionInfo();
                dropGoodsExceptionInfo.setCreateTime(LocalDateTime.now());
                dropGoodsExceptionInfo.setErrorNum(1);
                dropGoodsExceptionInfo.setMachineCode(machineCode);
                log.info("no alarm ,just save dropGoodsExceptionInfo：{}", dropGoodsExceptionInfo.toString());
                mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
            }
        }
    }

    /**
     * find phone by machineCode
     *
     * @param machineId
     * @return List
     */
    private List<Inno72CheckUserPhone> getInno72CheckUserPhones(String machineId) {
        Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
        inno72CheckUserPhone.setMachineCode(machineId);
        return checkUserService.selectPhoneByMachineCode(inno72CheckUserPhone);
    }

    /**
     * find locale by machineCode
     *
     * @param machineId
     * @return String
     */
    private String getLocaleString(String machineId) {
        List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
        MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
        machineLocaleInfo.setMachineCode(machineId);
        machineLocaleInfos.add(machineLocaleInfo);
        List<MachineLocaleInfo> machineLocaleInfoList = localeService.selectLocaleByMachineCode(machineLocaleInfos);
        String localStr = "";
        for (MachineLocaleInfo machineLocale : machineLocaleInfoList) {
            localStr = machineLocale.getLocaleStr();
        }
        return localStr;
    }
}
