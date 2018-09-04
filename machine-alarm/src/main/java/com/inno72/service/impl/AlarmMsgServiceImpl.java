package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72AlarmMsgMapper;
import com.inno72.model.Inno72AlarmMsg;
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

	@Override
	public List<Inno72AlarmMsg> findByPage(Object condition) {
		return null;
	}

	@Override
	public void saveAlarmMsg(String type, String system, String machineCode, int lackNum, String localStr) {
		Inno72AlarmMsg inno72AlarmMsg = new Inno72AlarmMsg();
		if ((CommonConstants.MACHINE_DROPGOODS_EXCEPTION).equals(type)) {
			inno72AlarmMsg.setTitle("您好，您负责的机器货道被锁定，请及时处理");
			inno72AlarmMsg.setType(2);
			inno72AlarmMsg.setDetail(localStr + "," + machineCode + "," + "出现掉货异常，请及时处理");
		} else if (CommonConstants.MACHINE_LACKGOODS_EXCEPTION.equals(type)) {
			inno72AlarmMsg.setTitle("您好，您负责的机器已缺货，请及时补货");
			inno72AlarmMsg.setType(3);
			inno72AlarmMsg.setDetail(localStr + "," + machineCode + "," + "缺货" + lackNum + "个，请及时处理");
		} else if (CommonConstants.SYS_MACHINE_NET.equals(type)){
			inno72AlarmMsg.setTitle("您好，您负责的机器出现网络异常，请及时处理");
			inno72AlarmMsg.setType(4);
			inno72AlarmMsg.setDetail("您好，"+localStr+"，机器编号："+machineCode+"，网络已经连续10分钟未连接成功，请及时联系巡检人员。");
		}
		inno72AlarmMsg.setCreateTime(LocalDateTime.now());
		inno72AlarmMsg.setSystem(system);
		inno72AlarmMsg.setMachineCode(machineCode);
		inno72AlarmMsg.setId(StringUtil.getUUID());
		inno72AlarmMsgMapper.insertSelective(inno72AlarmMsg);
	}
}
