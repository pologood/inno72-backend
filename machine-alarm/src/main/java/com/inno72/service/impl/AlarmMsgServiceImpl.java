package com.inno72.service.impl;

import java.time.LocalDateTime;
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
	public void saveAlarmMsg(String system,String machineCode, String detail,List<Inno72CheckUserPhone> inno72CheckUserPhones) {
		String title = "";
		int typeInt = 0;
		Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
		if ((CommonConstants.SYS_MACHINE_DROPGOODS).equals(system)) {
			title = "您好，您负责的机器货道被锁定，请及时处理";
			typeInt = 1;
		} else if (CommonConstants.SYS_MACHINE_LACKGOODS.equals(system)) {
			title = "您好，您负责的机器已缺货，请及时补货";
			typeInt = 2;
		} else if (CommonConstants.SYS_MACHINE_NET.equals(system)){
			title = "您好，您负责的机器出现网络异常，请及时处理";
			typeInt = 3;
		} else if(CommonConstants.SYS_MACHINE_HEART.equals(system)){
			title = "您好，您负责的机器出现页面异常，请及时处理";
			typeInt = 4;
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
		String appName = "machine_alarm";
		String androidStr = "";
		String iosStr = "";
		String text = JSON.toJSONString(inno72AlarmMsg);
		params.put("msg",text);
		for(Inno72CheckUserPhone checkUserPhone:inno72CheckUserPhones){
			String phone = checkUserPhone.getPhone();
			if(StringUtil.isNotEmpty(phone)){
				String androidPushKey = "push:android:" + phone;
				String iosPushKey = "push:ios:"+phone;
				Set<Object> androidPushSet = redisUtil.smembers(androidPushKey);
				Set<Object> iosPushSet = redisUtil.smembers(iosPushKey);
				if(androidPushSet != null && androidPushSet.size()>0){
					for(Object clientValue:androidPushSet){
						String clientValueStr = clientValue.toString();
						msgUtil.sendPush("push_android_check_app", params, clientValueStr, appName, title, detail);
						logger.info("按别名发送安卓手机push，接收者为："+clientValueStr+",title为："+title+"，内容为："+detail);
					}
				}
				if(iosPushSet != null && iosPushSet.size()>0){
					for(Object clientValue:iosPushSet){
						String clientValueStr = clientValue.toString();
						msgUtil.sendPush("push_ios_check_app", params, clientValueStr, appName, title, detail);
						logger.info("按别名发送苹果手机push，接收者为："+clientValueStr+",title为："+title+"，内容为："+detail);
					}
				}
			}
		}
	}
}
