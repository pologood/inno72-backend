package com.inno72.msgconsumer;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
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
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.machine.vo.PointLog;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.model.DropGoodsExceptionInfo;
import com.inno72.model.GoodsBean;
import com.inno72.model.Inno72AlarmGroup;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.model.MachineDropGoodsBean;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.msg.MsgUtil;
import com.inno72.service.AlarmGroupService;
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

    @Resource
    private AlarmGroupService alarmGroupService;

    @Value("${inno72.dingding.groupId}")
    private String groupId;

    public void receiveMessage(String message) throws UnsupportedEncodingException {


        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        String system = jsonObject.getString("system");
        log.info("receive msg:{}", message);
		String text = "";
		String goodsInfo = "";
		String active = System.getenv("spring_profiles_active");


        if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
                    });
            ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
            if(channelGoodsAlarmBean != null){
				String machineCode = channelGoodsAlarmBean.getMachineCode();
				Inno72Machine machine = machineService.findByCode(machineCode);
				Inno72AlarmGroup group = alarmGroupService.selectByParam(machine.getAreaCode());
				Boolean alarmFlag = setAlarmFlag(machine);
				int surPlusNum = channelGoodsAlarmBean.getSurPlusNum();
				List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
				Map<String, String> param = new HashMap<>();
				String localStr = channelGoodsAlarmBean.getLocaleStr();
				String goodsName = channelGoodsAlarmBean.getGoodsName();
				param.put("machineCode", machineCode);
				param.put("localStr", localStr);
				if(alarmFlag){
					List<GoodsBean> goodsBeanList = channelGoodsAlarmBean.getGoodsBeanList();
					if(goodsName.length()>3){
						goodsInfo+=goodsName.substring(goodsName.length()-3,goodsName.length())+"剩"+surPlusNum+"，";
					}else{
						goodsInfo+=goodsName+"剩"+surPlusNum+"，";
					}
					if(goodsBeanList != null && goodsBeanList.size()>0){
						for(GoodsBean goodsBean:goodsBeanList){
							String goods = goodsBean.getGoodsName();
							if(StringUtil.isNotEmpty(goods) && !goods.equals(goodsName)){
								int goodsSize = goods.length();
								if(goodsSize>3){
									goodsInfo +=goods.substring(goodsSize-3,goodsSize);
								}else{
									goodsInfo +=goods;
								}
								goodsInfo+="剩"+goodsBean.getTotalCount()+"，";
							}
						}
						if(goodsInfo.lastIndexOf("，")==goodsInfo.length()-1){
							goodsInfo = goodsInfo.substring(0,goodsInfo.length()-1);
						}
					}

					if(surPlusNum == 20){
						if (StringUtil.senSmsActive(active)) {
							text = goodsInfo;
							String address = machine.getAddress();
							if(StringUtil.isNotEmpty(address)){
								if(address.length()>10){
									address = address.substring(address.length()-10,address.length());
								}
								param.put("localStr", address);
							}
							param.put("text",  text);
							this.sendSms(param,machineCode,"sms_alarm_lackgoods");
						}
						text = "您好，"+machine.getLocaleStr()+"，机器编号："+machineCode+"，"+goodsInfo+"请及时联系巡检人员补货";
						param.put("text",StringUtil.setText(text,active));
						if(group != null){
							msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(), "machineAlarm-RedisReceiver");
							StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS,machineCode,text);
						}
					}else if(surPlusNum == 10 || surPlusNum == 5){
						text = "您好，"+machine.getLocaleStr()+"，机器编号："+machineCode+"，"+goodsInfo+"请及时联系巡检人员补货";
						param.put("text",StringUtil.setText(text,active));
						if(group != null){
							StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS,machineCode,text);
							msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(), "machineAlarm-RedisReceiver");
						}
					}
				}
			}
        }else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
            //接收并转数据类型
            AlarmMessageBean<MachineDropGoodsBean> alarmMessageBean = JSONObject.parseObject(message,
                    new TypeReference<AlarmMessageBean<MachineDropGoodsBean>>() {
                    });
            MachineDropGoodsBean machineDropGoods = alarmMessageBean.getData();
            String machineCode = machineDropGoods.getMachineCode();
			Inno72Machine machine = machineService.findByCode(machineCode);
			Inno72AlarmGroup group = alarmGroupService.selectByParam(machine.getAreaCode());
			Boolean alarmFlag = setAlarmFlag(machine);
			log.info("继续执行掉货异常报警。。。。");
			List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
            String channelNum = machineDropGoods.getChannelNum();

            Inno72SupplyChannel supplyChannel = supplyChannelService.selectByParam(machine.getId(),channelNum);
            log.info("machineDropGoods send msg ，machineCode：{}，channelNum：{}，describtion：{}", machineCode, channelNum, machineDropGoods.getDescribtion());
            //保存消息次数等信息
            Query query = new Query();
            query.addCriteria(Criteria.where("machineCode").is(machineCode));
            log.info("machineDropGoods send msg ，machineCode：{}", machineCode);
            List<DropGoodsExceptionInfo> dropGoodsExceptionInfoList = mongoTpl.find(query, DropGoodsExceptionInfo.class, "DropGoodsExceptionInfo");
            if (dropGoodsExceptionInfoList.size() > 0) {
                //根据机器编码查询点位接口
                String localStr = machine.getLocaleStr();
                log.info("machineDropGoods send msg ，localStr:{}", localStr);
                //循环
                for (DropGoodsExceptionInfo dropGoodsExceptionInfo : dropGoodsExceptionInfoList) {
                    Integer updateNum = dropGoodsExceptionInfo.getErrorNum() + 1;
                    log.info("machineDropGoods send msg ，save to mongo machineCode : {},num：{}", dropGoodsExceptionInfo.getMachineCode(), updateNum);
                    dropGoodsExceptionInfo.setErrorNum(updateNum);
                    mongoTpl.remove(query, "DropGoodsExceptionInfo");
                    mongoTpl.save(dropGoodsExceptionInfo, "DropGoodsExceptionInfo");
					supplyChannel.setIsDelete(1);
					supplyChannelService.closeSupply(supplyChannel);//锁货道
					List<Inno72SupplyChannel> normalSupplyList = supplyChannelService.selectNormalSupply(machine.getId(),channelNum);
                    //连续掉货两次
					if(alarmFlag){
						//巡检app接口
						Map<String, String> pushMap = new HashMap<>();
						pushMap.put("machineCode", machineCode);
						pushMap.put("localStr", localStr);
						pushMap.put("text", "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。");
						log.info("machineDropGoods send msg ，params：{}", pushMap.toString());
						//查询巡检人员手机号
						for (Inno72CheckUserPhone inno72CheckUserPhone1 : inno72CheckUserPhones) {
							String phone = inno72CheckUserPhone1.getPhone();
							msgUtil.sendPush("push_alarm_common", pushMap, phone, "machineAlarm-RedisReceiver", "【报警】您负责的机器出现掉货异常", "");
						}
						//钉钉报警
						Map<String, String> ddMaram = new HashMap<>();
						ddMaram.put("machineCode", machineCode);
						ddMaram.put("localStr", localStr);
						Map<String,String> smsMap = new HashMap<>();
						smsMap.put("machineCode", machineCode);
						String address = machine.getAddress();
						if(StringUtil.isNotEmpty(address)){
							if(address.length()>10){
								address = address.substring(address.length()-10,address.length());
							}
							smsMap.put("localStr", address);
						}
						if(normalSupplyList != null && normalSupplyList.size()>0){//有未被锁定的货道
							if(StringUtil.senSmsActive(active)){//生产，预发发送短信
								text = channelNum+"货道掉货异常，货道已经被锁定";
								smsMap.put("text",  text);
								this.sendSms(smsMap,machineCode,"sms_alarm_drop");
							}
							text = "您好，"+localStr+"，机器编号："+machineCode+"，"+channelNum+"掉货异常，货道已经被锁定，请及时联系巡检人员。";
						}else{//货道全部被锁
							if(StringUtil.senSmsActive(active)){//生产，预发发送短信
								text = channelNum+"货道掉货异常，"+supplyChannel.getGoodsName()+"所在货道已全部被锁定";
								smsMap.put("text",  text);
								this.sendSms(smsMap,machineCode,"sms_alarm_drop");
							}
							text = "您好，"+localStr+"，机器编号："+machineCode+"，"+supplyChannel.getGoodsName()+"所在的货道全部被锁定，请及时联系巡检人员处理。";
						}
						ddMaram.put("text",StringUtil.setText(text,active));
						if(group != null){
							//发送钉钉消息
							msgUtil.sendDDTextByGroup("dingding_alarm_common", ddMaram,group.getGroupId1() , "machineAlarm-RedisReceiver");
						}
					}
					//保存接口
					int lackNum = 0;
					alarmMsgService.saveAlarmMsg(type, system, machineCode, lackNum, localStr);
					if(alarmFlag) {
						StringUtil.logger(CommonConstants.LOG_TYPE_DROPGOODS, machineCode, text);
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
        }
    }

    /**
     * find phone by machineCode
     *
     * @param machineCode
     * @return List
     */
    private List<Inno72CheckUserPhone> getInno72CheckUserPhones(String machineCode) {
        Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
        inno72CheckUserPhone.setMachineCode(machineCode);
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

    public Boolean setAlarmFlag(Inno72Machine machine){
		log.info("机器信息：{}",JSON.toJSON(machine));
    	boolean alarmFlag = false;
		if(machine != null && machine.getOpenStatus() == 0){
			String monitorStart = machine.getMonitorStart();
			String monitorEnd = machine.getMonitorEnd();
			if(StringUtil.isNotEmpty(monitorStart) && StringUtil.isNotEmpty(monitorEnd)){
				Date now = new Date();
				Date startDate = null;
				String nowTime = DateUtil.toStrOld(now,DateUtil.DF_ONLY_YMD_S1_OLD);
				if(StringUtil.isNotEmpty(monitorStart)){
					String startTime = nowTime + " " +monitorStart;
					startDate = DateUtil.toDateOld(startTime,DateUtil.DF_ONLY_YMDHM);
				}
				Date endDate = null;
				if(StringUtil.isNotEmpty(monitorEnd)){
					String endTime = nowTime + " " +monitorEnd;
					endDate = DateUtil.toDateOld(endTime,DateUtil.DF_ONLY_YMDHM);
				}
				if((startDate != null && now.after(startDate) && (endDate != null && now.before(endDate)))){
					alarmFlag = true;//发送报警
				}
			}else{
				alarmFlag = true;
			}
		}
		log.info("返回报警标记"+alarmFlag);
		return alarmFlag;
	}


	public void sendSms(Map<String,String> params,String machineCode,String channel){
		List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
		for(Inno72CheckUserPhone userPhone :inno72CheckUserPhones){
			msgUtil.sendSMS(channel, params, userPhone.getPhone(), "machineAlarm-RedisReceiver");
		}
	}

}
