package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		inno72AlarmMsg.setTitle(title);
		inno72AlarmMsg.setType(typeInt);
		inno72AlarmMsg.setCreateTime(LocalDateTime.now());
		inno72AlarmMsg.setSystem(system);
		inno72AlarmMsg.setMachineCode(machineCode);
		inno72AlarmMsg.setId(StringUtil.getUUID());
		inno72AlarmMsgMapper.insertSelective(inno72AlarmMsg);
		Map<String,String> params = new HashMap<>();
		params.put("machineCode", machineCode);
		String appName = "machine_alarm";
		for(Inno72CheckUserPhone checkUserPhone:inno72CheckUserPhones){
			String phone = checkUserPhone.getPhone();
			if(StringUtil.isNotEmpty(phone)){
				String key = CommonConstants.CHECK_LOGIN_TYPE_KEY_PREF+phone;
				String loginType = redisUtil.get(key);
				if(StringUtil.isNotEmpty(loginType)){
					if(loginType.equals("android")){
						msgUtil.sendPush("push_android_check_app", params, phone, appName, title, detail);
					}else if(loginType.equals("ios")){
						msgUtil.sendPush("push_ios_check_app", params, phone, appName, title, detail);
					}
				}
			}
		}
	}
}
