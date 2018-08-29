package com.inno72.msgconsumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.StringUtil;
import com.inno72.model.*;
import com.inno72.msg.MsgUtil;
import com.inno72.service.*;
import org.apache.commons.lang.StringUtils;
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
import java.text.NumberFormat;
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

    @Resource
    private LocaleService localeService;

    @Resource
    private SupplyChannelStatusService supplyChannelStatusService;

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



        if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
                    });
            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
            int lackNum = channelGoodsAlarmBean.getLackNum();
            List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(channelGoodsAlarmBean.getMachineCode());
            Map<String, String> param = new HashMap<>();
            String localStr = channelGoodsAlarmBean.getLocaleStr();
            String machineCode = channelGoodsAlarmBean.getMachineCode();
            String goodsName = channelGoodsAlarmBean.getGoodsName();
            param.put("machineCode", channelGoodsAlarmBean.getMachineCode());
            param.put("localStr", localStr);
            String text = "";
            String active = System.getenv("spring_profiles_active");
            if(lackNum == 10){
                if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
                    text = "您好，【互动管家】您负责的机器，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于10个，请及时补货";
                    param.put("text",  text);
                    for(Inno72CheckUserPhone userPhone :inno72CheckUserPhones){
                        msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
                    }
                }

                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于10个，请及时补货";
                param.put("text",text);
                msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
            }else if(lackNum == 5){
                if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
                    text = "您好，【互动管家】您负责的机器，" + localStr + "，机器编号：" + machineCode + "，" + goodsName + "数量已少于5个，请及时补货";
                    param.put("text", text);
                    for (Inno72CheckUserPhone userPhone : inno72CheckUserPhones) {
                        msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-RedisReceiver");
                    }
                }
                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于5个，请及时补货";
                param.put("text",text);
                msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
            }else if(lackNum<5){
                text = "您好，"+localStr+"，机器编号："+machineCode+"，"+goodsName+"数量已少于5个，请及时补货";
                param.put("text",text);
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
                        params.put("text", "出现掉货异常，请及时处理");
                        log.info("machineDropGoods send msg ，params：{}", params.toString());
                        //查询巡检人员手机号
                        List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            String phone = inno72CheckUserPhone1.getPhone();
                            msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                        }

                        //保存接口
                        int lackNum = 0;
                        saveAlarmMsg(type, system, machineCode, lackNum, localStr);

                    } else if (updateNum == 5) {
                        //巡检app接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", "出现掉货异常，请及时处理");
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
                        saveAlarmMsg(type, system, machineCode, lackNum, localStr);

                        //钉钉报警
                        Map<String, String> param = new HashMap<>();
                        param.put("machineCode", machineCode);
                        param.put("localStr", localStr);
                        param.put("text", channelNum + "," + "出现掉货异常，请及时处理。");
                        msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");

                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
                        //钉钉报警接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", channelNum + "," + "出现掉货异常，请及时处理。");
                        msgUtil.sendDDTextByGroup("dingding_alarm_common", params, groupId, "machineAlarm-RedisReceiver");

                    }
                    Inno72Machine machine = machineService.findBy("machineCode",machineCode);
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










//        if ((CommonConstants.SYS_MACHINE_CHANNEL).equals(system)) {
//            //接收货道信息并转数据类型
//            AlarmMessageBean<MachineStatus> alarmMessageBean = JSONObject.parseObject(message,
//                    new TypeReference<AlarmMessageBean<MachineStatus>>() {
//                    });
//            MachineStatus machineStatus = alarmMessageBean.getData();
//            String goodsChannelStatus = machineStatus.getGoodsChannelStatus();
//            String machineId = machineStatus.getMachineId();
//            log.info("machineChannel send msg ，machineCode:{}", machineId);
//            //查询机器状态是否正常并且报警开关是否打开
//            Result<Inno72Machine> inno72MachineResult = machineService.findMachineByMachineCode(machineId);
//            Inno72Machine inno72Machine = inno72MachineResult.getData();
//            int openStatus = inno72Machine.getOpenStatus();
//            int status = inno72Machine.getMachineStatus();
//            if (CommonConstants.OPENSTATUS_OPEN == openStatus && CommonConstants.MACHINESTATUS_NUMAUL == status) {
//                //根据机器编码查询点位信息
//                String localStr = getLocaleString(machineId);
//                log.info("machineChannel send msg ，localStr:{}", localStr);
//                //查询故障信息
//                List<GoodsChannelBean> goodsChannelBean = JSON.parseArray(goodsChannelStatus, GoodsChannelBean.class);
//                List<GoodsChannelBean> goodsChannelBeans = supplyChannelStatusService.getChannelErrorDetail(goodsChannelBean);
//                //获取货道号与故障描述
//                for (GoodsChannelBean goodsChannel : goodsChannelBeans) {
//                    int channelNum = goodsChannel.getGoodsChannelNum();
//                    String describtion = goodsChannel.getDescription();
//                    Map<String, String> params = new HashMap<>();
//                    params.put("machineCode", machineId);
//                    params.put("localStr", localStr);
//                    params.put("text", "出现货道故障，货道编号是：" + channelNum + "，故障原因是：" + describtion + "，请及时处理。");
//                    log.info("machineChannel send msg ，params：{}", params.toString());
//                    //根据机器编码查询对应巡检人员
//                    List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineId);
//                    //企业微信提醒
//                    List<String> userIdList = new ArrayList<>();
//                    for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                        userIdList.add(inno72CheckUserPhone1.getUserId());
//                    }
//                    String userIdString = StringUtils.join(userIdList.toArray(), "|");
//                    log.info("machineChannel send msg ，userIdString:{}", userIdString);
//                    Map<String, String> m = new HashMap<>(16);
//                    m.put("touser", userIdString);
//                    m.put("agentid", "1000002");
//                    msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, "machineAlarm-RedisReceiver");
//                    //短信提醒
//                    String active = System.getenv("spring_profiles_active");
//                    log.info("machineChannel send msg ，获取spring_profiles_active：{}", active);
//                    if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
//                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                            String phone = inno72CheckUserPhone1.getPhone();
//                            msgUtil.sendSMS("sms_alarm_common", params, phone, "machineAlarm-RedisReceiver");
//                        }
//                    }
//                    //发巡检app提醒
//                    for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                        String phone = inno72CheckUserPhone1.getPhone();
//                        msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【故障】您负责的机器出现货道故障", "");
//                    }
//                    //保存接口
//                    int lackNum = 0;
//                    saveAlarmMsg(type, system, machineId, lackNum, localStr);
//
//                }
//
//            } else {
//                return;
//            }
//
//        } else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
//            //接收并转数据类型
//            AlarmMessageBean<MachineDropGoodsBean> alarmMessageBean = JSONObject.parseObject(message,
//                    new TypeReference<AlarmMessageBean<MachineDropGoodsBean>>() {
//                    });
//            MachineDropGoodsBean machineDropGoods = alarmMessageBean.getData();
//            String machineCode = machineDropGoods.getMachineCode();
//            String channelNum = machineDropGoods.getChannelNum();
//            log.info("machineDropGoods send msg ，machineCode：{}，channelNum：{}，describtion：{}", machineCode, channelNum, machineDropGoods.getDescribtion());
//            //保存消息次数等信息
//            Query query = new Query();
//            query.addCriteria(Criteria.where("machineCode").is(machineCode));
//            log.info("machineDropGoods send msg ，machineCode：{}", machineCode);
//            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class, "DropGoodsExceptionInfo");
//            if (dropGoodsExceptionInfoList.size() > 0) {
//                //根据机器编码查询点位接口
//                String localStr = getLocaleString(machineCode);
//                log.info("machineDropGoods send msg ，localStr:{}", localStr);
//                //循环
//                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
//                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
//                    log.info("machineDropGoods send msg ，save to mongo machineCode : {},num：{}", dropGoodsExceptionInfo.getMachineCode(), updateNum);
//                    dropGoodsExceptionInfo.setErrorNum(updateNum);
//                    mongoTpl.remove(query, "DropGoodsExceptionInfo");
//                    mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
//                    //连续掉货两次
//                    if (updateNum == 2) {
//                        //巡检app接口
//                        Map<String, String> params = new HashMap<>();
//                        params.put("machineCode", machineCode);
//                        params.put("localStr", localStr);
//                        params.put("text", "出现掉货异常，请及时处理");
//                        log.info("machineDropGoods send msg ，params：{}", params.toString());
//                        //查询巡检人员手机号
//                        List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
//                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                            String phone = inno72CheckUserPhone1.getPhone();
//                            msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
//                        }
//
//                        //保存接口
//                        int lackNum = 0;
//                        saveAlarmMsg(type, system, machineCode, lackNum, localStr);
//
//                    } else if (updateNum == 5) {
//                        //巡检app接口
//                        Map<String, String> params = new HashMap<>();
//                        params.put("machineCode", machineCode);
//                        params.put("localStr", localStr);
//                        params.put("text", "出现掉货异常，请及时处理");
//                        log.info("machineDropGoods send msg ，params：{}", params.toString());
//                        //查询巡检人员手机号
//                        List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
//
//                        //企业微信提醒
//                        List<String> userIdList = new ArrayList<>();
//                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                            userIdList.add(inno72CheckUserPhone1.getUserId());
//                        }
//                        String userIdString = StringUtils.join(userIdList.toArray(), "|");
//                        log.info("userIdString:{}", userIdString);
//                        Map<String, String> m = new HashMap<>();
//                        m.put("touser", userIdString);
//                        m.put("agentid", "1000002");
//                        msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, "machineAlarm-RedisReceiver");
//
//                        //巡检提醒
//                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                            String phone = inno72CheckUserPhone1.getPhone();
//                            msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
//                        }
//                        //保存接口
//                        int lackNum = 0;
//                        saveAlarmMsg(type, system, machineCode, lackNum, localStr);
//
//                        //钉钉报警
//                        Map<String, String> param = new HashMap<>();
//                        param.put("machineCode", machineCode);
//                        param.put("localStr", localStr);
//                        param.put("text", channelNum + "," + "出现掉货异常，请及时处理。");
//                        msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-RedisReceiver");
//
//                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
//                        //钉钉报警接口
//                        Map<String, String> params = new HashMap<>();
//                        params.put("machineCode", machineCode);
//                        params.put("localStr", localStr);
//                        params.put("text", channelNum + "," + "出现掉货异常，请及时处理。");
//                        msgUtil.sendDDTextByGroup("dingding_alarm_common", params, groupId, "machineAlarm-RedisReceiver");
//
//                    }
//                }
//            } else {
//                DropGoodsExceptionInfo dropGoodsExceptionInfo = new DropGoodsExceptionInfo();
//                dropGoodsExceptionInfo.setCreateTime(LocalDateTime.now());
//                dropGoodsExceptionInfo.setErrorNum(1);
//                dropGoodsExceptionInfo.setMachineCode(machineCode);
//                log.info("no alarm ,just save dropGoodsExceptionInfo：{}", dropGoodsExceptionInfo.toString());
//                mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
//            }
//
//
//        } else if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
//            //接收并转数据类型
//            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
//                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
//                    });
//            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
//            int lackNum = channelGoodsAlarmBean.getLackNum();
//            int surPlusNum = channelGoodsAlarmBean.getSurPlusNum();
//            int sum = lackNum + surPlusNum;
//            log.info("lackGoods msg，machineCode:{},lackNum:{},surPlusNum:{}", channelGoodsAlarmBean.getMachineCode(), lackNum, surPlusNum);
//            //根据机器编码查询点位接口
//            String localStr = getLocaleString(channelGoodsAlarmBean.getMachineCode());
//            log.info("lackGoods msg，localStr：{}", localStr);
//            NumberFormat numberFormat = NumberFormat.getInstance();
//            // 设置精确到小数点后2位
//            numberFormat.setMaximumFractionDigits(2);
//            String result = numberFormat.format((float) surPlusNum / (float) sum * 100);
//            float percent = Float.parseFloat(result);
//            log.info("machineDropGoods,percent:{}", percent);
//            Map<String, String> params = new HashMap<>();
//            params.put("machineCode", channelGoodsAlarmBean.getMachineCode());
//            params.put("localStr", localStr);
//            params.put("text", "缺货" + channelGoodsAlarmBean.getLackNum() + "个，请及时处理。");
//            log.info("machineDropGoods send msg ，params：{}", params.toString());
//            //缺货百分之二十到百分之十之间，巡检报警方式
//            if (percent > CommonConstants.TEN_PERSENT && percent <= CommonConstants.TWENTY_PERSENT) {
//                //查询巡检人员手机号
//                List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(channelGoodsAlarmBean.getMachineCode());
//
//                //企业微信提醒
//                List<String> userIdList = new ArrayList<>();
//                for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                    userIdList.add(inno72CheckUserPhone1.getUserId());
//                }
//                String userIdString = StringUtils.join(userIdList.toArray(), "|");
//                log.info("machineChannel send msg ，userIdString:{}", userIdString);
//                Map<String, String> m = new HashMap<>();
//                m.put("touser", userIdString);
//                m.put("agentid", "1000002");
//                msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, "machineAlarm-RedisReceiver");
//                //巡检app报警
//                for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                    String phone = inno72CheckUserPhone1.getPhone();
//                    msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【缺货】您负责的机器需要补货", "");
//                }
//
//                //保存接口
//                saveAlarmMsg(type, system, channelGoodsAlarmBean.getMachineCode(), channelGoodsAlarmBean.getLackNum(), localStr);
//
//            } else if (percent <= CommonConstants.TEN_PERSENT) {
//                //查询巡检人员手机号
//                List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(channelGoodsAlarmBean.getMachineCode());
//
//                //企业微信提醒
//                List<String> userIdList = new ArrayList<>();
//                for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                    userIdList.add(inno72CheckUserPhone1.getUserId());
//                }
//                String userIdString = StringUtils.join(userIdList.toArray(), "|");
//                log.info("userIdString:{}", userIdString);
//                Map<String, String> m = new HashMap<>();
//                m.put("touser", userIdString);
//                m.put("agentid", "1000002");
//                msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, "machineAlarm-RedisReceiver");
//
//                String active = System.getenv("spring_profiles_active");
//                log.info("获取spring_profiles_active：{}", active);
//                if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
//                    for (Inno72CheckUserPhone inno72CheckUserPhoneOne : inno72CheckUserPhones) {
//                        String phone = inno72CheckUserPhoneOne.getPhone();
//                        msgUtil.sendSMS("sms_alarm_common", params, phone, "machineAlarm-RedisReceiver");
//                    }
//                }
//                //巡检消息
//                for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
//                    String phone = inno72CheckUserPhone1.getPhone();
//                    msgUtil.sendPush("push_alarm_common", params, phone, "machineAlarm-RedisReceiver", "【缺货】您负责的机器需要补货", "");
//                }
//
//                //保存接口
//                saveAlarmMsg(type, system, channelGoodsAlarmBean.getMachineCode(), channelGoodsAlarmBean.getLackNum(), localStr);
//
//            }
//
//        } else {
//            return;
//        }

    }

    /**
     * save alarm msg
     *
     * @param
     * @return
     */
    private void saveAlarmMsg(String type, String system, String machineCode, int lackNum, String localStr) {
        Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
        if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
            inno72AlarmMsg.setTitle("您负责的机器出现掉货异常");
            inno72AlarmMsg.setType(2);
            inno72AlarmMsg.setDetail(localStr + "," + machineCode + "," + "出现掉货异常，请及时处理");
        } else if (CommonConstants.MACHINE_LACKGOODS_EXCEPTION.equals(type)) {
            inno72AlarmMsg.setTitle("您负责的机器需要补货");
            inno72AlarmMsg.setType(3);
            inno72AlarmMsg.setDetail(localStr + "," + machineCode + "," + "缺货" + lackNum + "个，请及时处理");
        }
        inno72AlarmMsg.setCreateTime(LocalDateTime.now());
        inno72AlarmMsg.setSystem(system);
        inno72AlarmMsg.setMachineCode(machineCode);
        inno72AlarmMsg.setId(StringUtil.getUUID());
        alarmMsgService.save(inno72AlarmMsg);
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
