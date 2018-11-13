package com.inno72.msgconsumer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmMessageBean;
import com.inno72.model.ChannelGoodsAlarmBean;
import com.inno72.model.GoodsBean;
import com.inno72.model.Inno72AlarmGroup;
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

	public void receiveMessage(String message) throws UnsupportedEncodingException {

		JSONObject jsonObject = JSON.parseObject(message);
		String type = jsonObject.getString("type");
		String system = jsonObject.getString("system");
		log.info("receive msg:{}", message);
		String text = "";
		String goodsInfo = "";
		String active = System.getenv("spring_profiles_active");

		if ((CommonConstants.SYS_MACHINE_LACKGOODS).equals(system)) {
			// 接收并转数据类型
			AlarmMessageBean<ChannelGoodsAlarmBean> alarmMessageBean = JSONObject.parseObject(message,
					new TypeReference<AlarmMessageBean<ChannelGoodsAlarmBean>>() {
					});
			ChannelGoodsAlarmBean channelGoodsAlarmBean = alarmMessageBean.getData();
			if (channelGoodsAlarmBean != null) {
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
				if (alarmFlag) {
					List<GoodsBean> goodsBeanList = channelGoodsAlarmBean.getGoodsBeanList();
					goodsInfo += goodsName + "剩" + surPlusNum + "，";
					if (goodsBeanList != null && goodsBeanList.size() > 0) {
						for (GoodsBean goodsBean : goodsBeanList) {
							String goods = goodsBean.getGoodsName();
							if (StringUtil.isNotEmpty(goods) && !goods.equals(goodsName)) {
								goodsInfo += goods+"剩" + goodsBean.getTotalCount() + "，";
							}
						}
						if (goodsInfo.lastIndexOf("，") == goodsInfo.length() - 1) {
							goodsInfo = goodsInfo.substring(0, goodsInfo.length() - 1);
						}
					}
					if(surPlusNum == 20 || surPlusNum == 10 || surPlusNum == 5){
						Map<String,String> pushMap = new HashMap<>();
						if (surPlusNum == 20) {
							if (StringUtil.senSmsActive(active)) {
								text = goodsInfo;
								String address = machine.getAddress();
								if (StringUtil.isNotEmpty(address)) {
									if (address.length() > 10) {
										address = address.substring(address.length() - 10, address.length());
									}
									param.put("localStr", address);
								}
								param.put("text", text);
								this.sendSms(param, machineCode, "sms_alarm_lackgoods");
							}
							text = "您好，" + machine.getLocaleStr() + "，机器编号：" + machineCode + "，" + goodsInfo
									+ "请及时联系巡检人员补货";
							param.put("text", StringUtil.setText(text, active));
							if (group != null) {
								text = "缺货报警，提醒方式：钉钉和短信，内容：您好，" + localStr + "，机器编号：" + machineCode + "," + goodsName
										+ "数量已少于" + surPlusNum + "，请及时补货。";
								StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS, machineCode, text);
								log.info("发送缺货" + surPlusNum + "报警日志，日志内容为：{}", machineCode, text);
								msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(),
										"machineAlarm-RedisReceiver");
							}
						} else if (surPlusNum == 10 || surPlusNum == 5) {
							text = "您好，" + machine.getLocaleStr() + "，机器编号：" + machineCode + "，" + goodsInfo
									+ "请及时联系巡检人员补货";
							param.put("text", StringUtil.setText(text, active));
							if (group != null) {
								text = "缺货报警，提醒方式：钉钉，内容：您好，" + localStr + "，机器编号：" + machineCode + "," + goodsName + "数量已少于"
										+ surPlusNum + "，请及时补货。";
								StringUtil.logger(CommonConstants.LOG_TYPE_LACKGOODS, machineCode, text);
								log.info("发送缺货" + surPlusNum + "报警日志，日志内容为：{}", machineCode, text);
								msgUtil.sendDDTextByGroup("dingding_alarm_common", param, group.getGroupId2(),
										"machineAlarm-RedisReceiver");
							}
						}
						alarmMsgService.saveAlarmMsg(system,machineCode,text,inno72CheckUserPhones);
					}

				}
			}
		} else if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
			// 接收并转数据类型
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
			String[] channelArray = null;
			if (StringUtil.isNotEmpty(channelNum)) {
				channelArray = channelNum.split(",");
			}

			String localStr = machine.getLocaleStr();
			List<Inno72SupplyChannel> supplyChannelList = supplyChannelService.selectByParam(machine.getId(),
					channelArray);
			log.info("machineDropGoods send msg ，machineCode：{}，channelNum：{}，describtion：{}", machineCode, channelNum,
					machineDropGoods.getDescribtion());
			String goodsId = null;
			String goodsName = null;
			if (supplyChannelList != null && supplyChannelList.size() > 0) {
				for (Inno72SupplyChannel inno72SupplyChannel : supplyChannelList) {
					inno72SupplyChannel.setIsDelete(1);
					goodsName = inno72SupplyChannel.getGoodsName();
					goodsId = inno72SupplyChannel.getGoodsId();
					supplyChannelService.closeSupply(inno72SupplyChannel);// 锁货道
				}
			}
			List<Inno72SupplyChannel> normalSupplyList = supplyChannelService.selectNormalSupply(machine.getId(),
					goodsId);
			if (alarmFlag) {
				// 巡检app接口
				Map<String, String> pushMap = new HashMap<>();
				pushMap.put("machineCode", machineCode);
				pushMap.put("localStr", localStr);
				pushMap.put("text",
						"您好，" + localStr + "，机器编号：" + machineCode + "，" + channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。");
				log.info("machineDropGoods send msg ，params：{}", pushMap.toString());
				// 钉钉报警
				Map<String, String> ddMaram = new HashMap<>();
				ddMaram.put("machineCode", machineCode);
				ddMaram.put("localStr", localStr);
				Map<String, String> smsMap = new HashMap<>();
				smsMap.put("machineCode", machineCode);
				String address = machine.getAddress();
				if (StringUtil.isNotEmpty(address)) {
					if (address.length() > 10) {
						address = address.substring(address.length() - 10, address.length());
					}
					smsMap.put("localStr", address);
				}
				if (normalSupplyList != null && normalSupplyList.size() > 0) {// 有未被锁定的货道
					text = "您好，" + localStr + "，机器编号：" + machineCode + "，" + channelNum + "掉货异常，货道已经被锁定，请及时联系巡检人员。";
				} else {// 货道全部被锁
					if (StringUtil.senSmsActive(active)) {// 生产，预发发送短信
						text = "货道" + channelNum + "掉货异常，" + goodsName + "所在货道已全部被锁定";
						smsMap.put("text", text);
						this.sendSms(smsMap, machineCode, "sms_alarm_drop");
					}
					text = "您好，" + localStr + "，机器编号：" + machineCode + "，" + goodsName + "所在的货道全部被锁定，请及时联系巡检人员处理。";
				}
				ddMaram.put("text", StringUtil.setText(text, active));
				log.info("group值为：{}", JSON.toJSON(group));
				if (group != null) {
					// 发送钉钉消息
					log.info("发送钉钉消息{}", JSON.toJSON(ddMaram));
					msgUtil.sendDDTextByGroup("dingding_alarm_common", ddMaram, group.getGroupId1(),
							"machineAlarm-RedisReceiver");
					text = "掉货异常，提醒方式：短信和钉钉，内容：您好，" + localStr + "机器编号：" + machineCode + "," + channelNum
							+ "掉货异常，货道已被锁定，请及时联系巡检人员。";
					StringUtil.logger(CommonConstants.LOG_TYPE_DROPGOODS, machineCode, text);
					log.info("发送掉货异常埋点日志", CommonConstants.LOG_TYPE_DROPGOODS, machineCode, text);
				}
			}
			// 保存接口
			int lackNum = 0;
			alarmMsgService.saveAlarmMsg(system, machineCode,text, inno72CheckUserPhones);
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

	public Boolean setAlarmFlag(Inno72Machine machine) {
		log.info("机器信息：{}", JSON.toJSON(machine));
		boolean alarmFlag = false;
		if (machine != null && machine.getOpenStatus() == 0) {
			String monitorStart = machine.getMonitorStart();
			String monitorEnd = machine.getMonitorEnd();
			if (StringUtil.isNotEmpty(monitorStart) && StringUtil.isNotEmpty(monitorEnd)) {
				Date now = new Date();
				Date startDate = null;
				String nowTime = DateUtil.toStrOld(now, DateUtil.DF_ONLY_YMD_S1_OLD);
				if (StringUtil.isNotEmpty(monitorStart)) {
					String startTime = nowTime + " " + monitorStart;
					startDate = DateUtil.toDateOld(startTime, DateUtil.DF_ONLY_YMDHM);
				}
				Date endDate = null;
				if (StringUtil.isNotEmpty(monitorEnd)) {
					String endTime = nowTime + " " + monitorEnd;
					endDate = DateUtil.toDateOld(endTime, DateUtil.DF_ONLY_YMDHM);
				}
				if ((startDate != null && now.after(startDate) && (endDate != null && now.before(endDate)))) {
					alarmFlag = true;// 发送报警
				}
			} else {
				alarmFlag = true;
			}
		}
		log.info("返回报警标记" + alarmFlag);
		return alarmFlag;
	}

	public void sendSms(Map<String, String> params, String machineCode, String channel) {
		List<Inno72CheckUserPhone> inno72CheckUserPhones = getInno72CheckUserPhones(machineCode);
		for (Inno72CheckUserPhone userPhone : inno72CheckUserPhones) {
			msgUtil.sendSMS(channel, params, userPhone.getPhone(), "machineAlarm-RedisReceiver");
		}
	}


	public void sendPush(List<Inno72CheckUserPhone> inno72CheckUserPhones,Map<String,String> pushMap,String title){
		for (Inno72CheckUserPhone inno72CheckUserPhone :
				inno72CheckUserPhones) {
			String phone = inno72CheckUserPhone.getPhone();
			msgUtil.sendPush("push_alarm_common", pushMap, phone,
					"machineAlarm-RedisReceiver", title, "");
		}
	}

}
