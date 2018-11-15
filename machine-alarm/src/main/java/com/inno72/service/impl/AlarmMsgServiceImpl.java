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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
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
	public void saveAlarmMsg(String system, String machineCode, int lackNum, String localStr,List<Inno72CheckUserPhone> inno72CheckUserPhones) {
		String title = "";
		int typeInt = 0;
		String detail = "";
		Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
		if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
			title = "您好，您负责的机器货道被锁定，请及时处理";
			typeInt = 2;
			detail = localStr + "," + machineCode + "," + "出现掉货异常，请及时处理";
		} else if (CommonConstants.SYS_MACHINE_LACKGOODS.equals(system)) {
			title = "您好，您负责的机器已缺货，请及时补货";
			typeInt = 3;
			detail = localStr + "," + machineCode + "," + "缺货" + lackNum + "个，请及时处理";
		} else if (CommonConstants.SYS_MACHINE_NET.equals(system)){
			title = "您好，您负责的机器出现网络异常，请及时处理";
			typeInt = 4;
			detail = "您好，"+localStr+"，机器编号："+machineCode+"，网络已经连续10分钟未连接成功，请及时联系巡检人员。";
		}
		inno72AlarmMsg.setDetail(detail);
		inno72AlarmMsg.setMainType(1);
		inno72AlarmMsg.setChildType(typeInt);
		inno72AlarmMsg.setTitle(title);
		inno72AlarmMsg.setCreateTime(LocalDateTime.now());
		inno72AlarmMsg.setSystem(system);
		inno72AlarmMsg.setMachineCode(machineCode);
		inno72AlarmMsg.setId(StringUtil.getUUID());
		inno72AlarmMsgMapper.insertSelective(inno72AlarmMsg);
		logger.info("存储消息"+title);
		Map<String,String> params = new HashMap<>();
		params.put("machineCode", machineCode);
		String appName = "machine_alarm";
		List<String> androidTagList = new ArrayList<>();
		List<String> iosTagList = new ArrayList<>();
		for(Inno72CheckUserPhone checkUserPhone:inno72CheckUserPhones){
			String phone = checkUserPhone.getPhone();
			if(StringUtil.isNotEmpty(phone)){
				String androidKey = "push:android:" + phone;
				String iosKey = "push:ios:"+phone;
				String androidValue = redisUtil.get(androidKey);
				String iosValue = redisUtil.get(iosKey);
				if(StringUtil.isNotEmpty(androidValue)){
					androidTagList.add(phone);
				}
				if(StringUtil.isNotEmpty(iosValue)){
					iosTagList.add(phone);
				}
			}
		}
		if(androidTagList != null && androidTagList.size()>0){
			sendPushTagList(JSON.toJSONString(inno72AlarmMsg),2,androidTagList,"CheckAppMessage_toApp");
		}
		if(iosTagList != null && iosTagList.size()>0){
			sendPushTagList(JSON.toJSONString(inno72AlarmMsg),2,iosTagList,"CheckAppMessage_toApp");
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
		jsonObject.put("childType",0);
		jsonObject.put("createTime",LocalDateTime.now());
		jsonObject.put("detail","消息详情");
		jsonObject.put("id","6785678765678");
		jsonObject.put("machineCode","13995322");
		jsonObject.put("mainType",1);
		jsonObject.put("system","machineNoHeart");
		jsonObject.put("title","yyyyyyy");
		transmissionTemplate.setTransmissionContent(jsonObject.toJSONString());
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplate);
		message.setOffline(true); //离线有效时间，单位为毫秒，可选 message.setOfflineExpireTime(24 * 1000 * 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		//⼿手机类型
		List<String> tagList = new ArrayList<String>();
		tagList.add("13716944223");
		cdt.addCondition(AppConditions.TAG,tagList);
		message.setConditions(cdt);
		IPushResult ret = push.pushMessageToApp(message,"CheckAppMessage_toApp");
		System.out.println(ret.getResponse().toString());



	}

	/**
	 *
	 * @param transmissionContent 透传json串
	 * @param tagList 标签集合
	 * @param taskGroupName 群组名
	 */
	public static void sendPushTagList(String transmissionContent,int transmissionType,List<String> tagList,String taskGroupName){
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		TransmissionTemplate transmissionTemplate = new TransmissionTemplate();
		transmissionTemplate.setTransmissionType(transmissionType);
		transmissionTemplate.setAppId(appId);
		transmissionTemplate.setAppkey(appKey);
		transmissionTemplate.setTransmissionContent(transmissionContent);
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplate);
		message.setOffline(true); //离线有效时间，单位为毫秒，可选 message.setOfflineExpireTime(24 * 1000 * 3600); //推送给App的⽬目标⽤用户需要满⾜足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		cdt.addCondition(AppConditions.TAG,tagList);
		message.setConditions(cdt);
		IPushResult ret = push.pushMessageToApp(message,taskGroupName);
		System.out.println(ret.getResponse().toString());
	}
}
