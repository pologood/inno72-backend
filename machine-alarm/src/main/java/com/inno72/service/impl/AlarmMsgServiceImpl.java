package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72AlarmMsgMapper;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmMsgService;

/**
 * Created by CodeGenerator on 2018/07/19.
 *
 * @author
 */
@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {
	@Resource
	private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private MsgUtil msgUtil;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Inno72AlarmMsg> findByPage(Object condition) {
		return null;
	}

	@Override
	public void saveAlarmMsg(String system, String machineCode,String title ,String textBeaf, String detail,
			List<Inno72CheckUserPhone> inno72CheckUserPhones) {
		int typeInt = 0;
		Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
		String titleLast = "";
		String titleBeaf = "";
		String titleMiddel = "机器："+machineCode;
		if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
			titleBeaf = "【异常】";
			titleLast = "，请及时处理";
			typeInt = 1;
		} else if (CommonConstants.SYS_MACHINE_LACKGOODS.equals(system)) {
			titleBeaf = "【补货】";
			titleLast = "，请及时补货";
			typeInt = 2;
		} else if (CommonConstants.SYS_MACHINE_NET.equals(system)) {
			titleBeaf = "【报警】";
			titleLast = "，请及时处理";
			typeInt = 3;
		} else if (CommonConstants.SYS_MACHINE_HEART.equals(system)) {
			titleBeaf = "【报警】";
			titleLast = "，请及时处理";
			typeInt = 4;
		}
		LocalDateTime nowTime = LocalDateTime.now();
		String id = StringUtil.getUUID();
		inno72AlarmMsg.setDetail(detail);
		inno72AlarmMsg.setMainType(1);
		inno72AlarmMsg.setChildType(typeInt);
		inno72AlarmMsg.setTitle(titleBeaf+title);
		inno72AlarmMsg.setCreateTime(nowTime);
		inno72AlarmMsg.setSystem(system);
		inno72AlarmMsg.setMachineCode(machineCode);
		inno72AlarmMsg.setId(id);
		inno72AlarmMsgMapper.insertSelective(inno72AlarmMsg);
		logger.info("存储消息" + title);
		Map<String, String> params = new HashMap<>();
		String appName = "machine_alarm";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("detail", detail);
		jsonObject.put("mainType", 1);
		jsonObject.put("childType", typeInt);
		jsonObject.put("title", titleBeaf+titleMiddel+title+titleLast);
		jsonObject.put("createTime", DateUtil.toTimeStr(nowTime, DateUtil.DF_FULL_S1));
		jsonObject.put("system", system);
		jsonObject.put("machineCode", machineCode);
		jsonObject.put("id", id);
		String text = JSONObject.toJSONString(jsonObject);
		params.put("msg", text);
		for (Inno72CheckUserPhone checkUserPhone : inno72CheckUserPhones) {
			String phone = checkUserPhone.getPhone();
			if (StringUtil.isNotEmpty(phone)) {
				String androidPushKey = "push:android:" + phone;
				String iosPushKey = "push:ios:" + phone;
				Set<Object> androidPushSet = redisUtil.smembers(androidPushKey);
				Set<Object> iosPushSet = redisUtil.smembers(iosPushKey);
				if (androidPushSet != null && androidPushSet.size() > 0) {
					for (Object clientValue : androidPushSet) {
						String clientValueStr = clientValue.toString();
						msgUtil.sendPush("push_android_check_app", params, clientValueStr, appName, title, detail);
						logger.info("按别名发送安卓手机push，接收者为：" + clientValueStr + ",title为：" + title + "，内容为：" + detail);
					}
				}
				if (iosPushSet != null && iosPushSet.size() > 0) {
					for (Object clientValue : iosPushSet) {
						String clientValueStr = clientValue.toString();
						msgUtil.sendPush("push_ios_check_app", params, clientValueStr, appName, "", title);
						logger.info("按别名发送苹果手机push，接收者为：" + clientValueStr + ",title为：" + title + "，内容为：" + detail);
					}
				}
			}
		}
	}

	private static String appId = "vxa494yf3Z7cb22lmvIxq2";
	private static String appKey = "qPXgOKKzFkAxtUD5IhDLk2";
	private static String masterSecret = "sqA0pWF3qU5rtlwWErbGg";
	static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void main(String[] args) throws Exception {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
		transmissionTemplate.setTransmissionType(2);
		transmissionTemplate.setAppId(appId);
		transmissionTemplate.setAppkey(appKey);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("childType", 0);
		jsonObject.put("createTime", LocalDateTime.now());
		jsonObject.put("detail", "消息详情");
		jsonObject.put("id", "6785678765678");
		jsonObject.put("machineCode", "13995322");
		jsonObject.put("mainType", 1);
		jsonObject.put("system", "machineNoHeart");
		jsonObject.put("title", "yyyyyyy");
		transmissionTemplate.setTransmissionContent(jsonObject.toJSONString());
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplate);
		message.setOffline(true); // 离线有效时间，单位为毫秒，可选
									// message.setOfflineExpireTime(24 * 1000 *
									// 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		// ⼿手机类型
		List<String> tagList = new ArrayList<String>();
		tagList.add("13716944223");
		cdt.addCondition(AppConditions.TAG, tagList);
		message.setConditions(cdt);
		IPushResult ret = push.pushMessageToApp(message, "CheckAppMessage_toApp");
		System.out.println(ret.getResponse().toString());

	}

	/**
	 *
	 * @param transmissionContent
	 *            透传json串
	 * @param tagList
	 *            标签集合
	 * @param taskGroupName
	 *            群组名
	 */
	public static void sendPushTagList(String transmissionContent, int transmissionType, List<String> tagList,
			String taskGroupName) {
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
		transmissionTemplate.setTransmissionType(transmissionType);
		transmissionTemplate.setAppId(appId);
		transmissionTemplate.setAppkey(appKey);
		transmissionTemplate.setTransmissionContent(transmissionContent);
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplate);
		message.setOffline(true); // 离线有效时间，单位为毫秒，可选
									// message.setOfflineExpireTime(24 * 1000 *
									// 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		cdt.addCondition(AppConditions.TAG, tagList);
		message.setConditions(cdt);
		IPushResult ret = push.pushMessageToApp(message, taskGroupName);
		System.out.println(ret.getResponse().toString());
	}
}
