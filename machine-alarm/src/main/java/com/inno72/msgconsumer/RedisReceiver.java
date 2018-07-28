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
import com.inno72.service.LocaleService;
import com.inno72.service.SupplyChannelStatusService;
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

    @Resource
    private LocaleService localeService;

    @Resource
    private SupplyChannelStatusService supplyChannelStatusService;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    public void receiveMessage(String message) throws UnsupportedEncodingException {


        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        String system = jsonObject.getString("system");
        log.info("receive msg:{}", message);

        if ((CommonConstants.SYS_MACHINE_CHANNEL).equals(system)) {
            //接收货道信息并转数据类型
            AlarmMessageBean<MachineStatus> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineStatus>>() {
                    });
            MachineStatus machineStatus = alarmMessageBean.getData();
            String goodsChannelStatus = machineStatus.getGoodsChannelStatus();
            String machineId = machineStatus.getMachineId();
            log.info("machineChannel msg，machineCode:{}", machineId);
            //根据机器编码查询点位信息
            List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
            MachineLocaleInfo machineLocaleInfo = new MachineLocaleInfo();
            machineLocaleInfo.setMachineCode(machineId);
            machineLocaleInfos.add(machineLocaleInfo);
            List<MachineLocaleInfo> machineLocaleInfoList = localeService.selectLocaleByMachineCode(machineLocaleInfos);
            String localStr = "";
            for (MachineLocaleInfo machineLocale : machineLocaleInfoList) {
                localStr = machineLocale.getLocaleStr();
            }
            log.info("machineChannel msg，localStr:{}", localStr);
            //查询故障信息
            List<GoodsChannelBean> goodsChannelBean = JSON.parseArray(goodsChannelStatus, GoodsChannelBean.class);
            List<GoodsChannelBean> goodsChannelBeans = supplyChannelStatusService.getChannelErrorDetail(goodsChannelBean);
            //获取货道号与故障描述
            for (GoodsChannelBean goodsChannel : goodsChannelBeans) {
                int channelNum = goodsChannel.getGoodsChannelNum();
                String describtion = goodsChannel.getDescription();
                String code = "sms_alarm_common";
                Map<String, String> params = new HashMap<>();
                params.put("machineCode", machineId);
                params.put("localStr", localStr);
                params.put("text", "出现掉货异常，货道编号是：" + channelNum + "，故障原因是：" + describtion + "，请及时处理。");
                log.info("machineChannel send duanxin ，params：{}", params.toString());
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

        } else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<MachineDropGoodsBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineDropGoodsBean>>() {
                    });
            MachineDropGoodsBean machineDropGoods = alarmMessageBean.getData();
            String machineCode = machineDropGoods.getMachineCode();
            String channelNum = machineDropGoods.getChannelNum();
            String describtion = machineDropGoods.getDescribtion();
            log.info("to dropGoods msg，machineCode：{}，channelNum：{}，describtion：{}", machineCode, channelNum, describtion);
            //保存消息次数等信息
            Query query = new Query();
            query.addCriteria(Criteria.where("machineCode").is(machineCode));
            log.info("dropGoods query condition,machineCode：{}", machineCode);
            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class, "DropGoodsExceptionInfo");
            log.info("当前数据库查询数据，dropGoodsExceptionInfoList：{}", dropGoodsExceptionInfoList.toString());
            if (dropGoodsExceptionInfoList.size() > 0) {
                //根据机器编码查询点位接口
                List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
                MachineLocaleInfo machineLocale = new MachineLocaleInfo();
                machineLocale.setMachineCode(machineCode);
                machineLocaleInfos.add(machineLocale);
                List<MachineLocaleInfo> machineLocaleInfoList = localeService.selectLocaleByMachineCode(machineLocaleInfos);
                String localStr = "";
                for (MachineLocaleInfo machineLocaleInfo : machineLocaleInfoList) {
                    localStr = machineLocaleInfo.getLocaleStr();
                }
                log.info("dropGoods msg，localStr：{}", localStr);
                //循环
                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
                    log.info("save to mongo machineCode : {},num：{}", dropGoodsExceptionInfo.getMachineCode(), updateNum);
                    dropGoodsExceptionInfo.setErrorNum(updateNum);
                    mongoTpl.remove(query, "DropGoodsExceptionInfo");
                    mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
                    //连续掉货两次
                    if (updateNum == 2) {
                        //巡检app接口
                        String code = "push_alarm_common";
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", "出现掉货异常，请及时处理");
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
                            msgUtil.sendPush(code, params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                        }

                        //保存接口
                        Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
                        if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
                            inno72AlarmMsg.setTitle("报警");
                            inno72AlarmMsg.setType(2);
                        }
                        inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                        inno72AlarmMsg.setSystem(system);
                        inno72AlarmMsg.setMachineCode(machineCode);
                        inno72AlarmMsg.setId(StringUtil.getUUID());
                        inno72AlarmMsg.setDetail(machineCode + "," + "出现掉货异常，请及时处理");
                        alarmMsgService.save(inno72AlarmMsg);

                    } else if (updateNum == 5) {
                        //组合报警接口
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", channelNum + "," + describtion + "出现掉货异常，请及时处理");
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
                        for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                            String phone = inno72CheckUserPhone1.getPhone();
                            String code = "push_alarm_common";
                            msgUtil.sendPush(code, params, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
                        }

                        //保存接口
                        Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
                        if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
                            inno72AlarmMsg.setTitle("报警");
                            inno72AlarmMsg.setType(2);
                        }
                        inno72AlarmMsg.setCreateTime(LocalDateTime.now());
                        inno72AlarmMsg.setSystem(system);
                        inno72AlarmMsg.setMachineCode(machineCode);
                        inno72AlarmMsg.setId(StringUtil.getUUID());
                        inno72AlarmMsg.setDetail(machineCode + "," + channelNum + "," + describtion + "出现掉货异常，请及时处理");
                        alarmMsgService.save(inno72AlarmMsg);

                    } else if (updateNum > 5 && (updateNum - 5) % 2 == 0) {
                        //钉钉报警接口
                        String code = "dingding_alarm_common";
                        Map<String, String> params = new HashMap<>();
                        params.put("machineCode", machineCode);
                        params.put("localStr", localStr);
                        params.put("text", channelNum + "," + describtion + "出现掉货异常，请及时处理。");
                        msgUtil.sendDDTextByGroup(code, params, groupId, "machineAlarm-RedisReceiver");

                    }
                }
            } else {
                DropGoodsExceptionInfo dropGoodsExceptionInfo = new DropGoodsExceptionInfo();
                dropGoodsExceptionInfo.setCreateTime(LocalDateTime.now());
                dropGoodsExceptionInfo.setErrorNum(1);
                dropGoodsExceptionInfo.setMachineCode(machineCode);
                log.info("no alarm ,just save dropGoodsExceptionInfo：{}", dropGoodsExceptionInfo.toString());
                mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
            }


        } else if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
                    });
            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
            log.info("lackGoods msg，machineCode:{}", channelGoodsAlarmBean.getMachineCode());

            //根据机器编码查询点位接口
            List<MachineLocaleInfo> machineLocaleInfos = new ArrayList<>();
            MachineLocaleInfo machineLocale = new MachineLocaleInfo();
            machineLocale.setMachineCode(channelGoodsAlarmBean.getMachineCode());
            machineLocaleInfos.add(machineLocale);
            List<MachineLocaleInfo> machineLocaleInfoList = localeService.selectLocaleByMachineCode(machineLocaleInfos);
            String localStr = "";
            for (MachineLocaleInfo machineLocaleInfo : machineLocaleInfoList) {
                localStr = machineLocaleInfo.getLocaleStr();
            }
            log.info("lackGoods msg，localStr：{}", localStr);
            //调用报警接口
            //缺货个数
            //组合报警接口
            Map<String, String> params = new HashMap<>();
            params.put("machineCode", channelGoodsAlarmBean.getMachineCode());
            params.put("localStr", localStr);
            params.put("text", "缺货" + channelGoodsAlarmBean.getLackNum() + "个，请及时处理。");
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
            for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
                String phone = inno72CheckUserPhone1.getPhone();
                String code = "push_alarm_common";
                msgUtil.sendPush(code, params, phone, "machineAlarm-CheckNetAndAlarmTask", "【缺货】您负责的机器需要补货", "");
            }

            //保存接口
            Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
            if ((CommonConstants.MACHINE_LACKGOODS_EXCEPTION).equals(type)) {
                inno72AlarmMsg.setTitle("补货");
                inno72AlarmMsg.setType(3);
            }
            inno72AlarmMsg.setMachineCode(channelGoodsAlarmBean.getMachineCode());
            inno72AlarmMsg.setCreateTime(LocalDateTime.now());
            inno72AlarmMsg.setSystem(system);
            inno72AlarmMsg.setDetail(channelGoodsAlarmBean.getMachineCode() + "," + "缺货" + channelGoodsAlarmBean.getLackNum() + "个，请及时处理");
            inno72AlarmMsg.setId(StringUtil.getUUID());
            alarmMsgService.save(inno72AlarmMsg);

        } else {
            return;
        }

    }
}
