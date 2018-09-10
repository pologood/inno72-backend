package com.inno72.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.Inno72Machine;
import com.inno72.model.SystemStatus;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.SocketService;
import com.inno72.util.AlarmUtil;

import tk.mybatis.mapper.entity.Condition;

@Service
public class SocketServiceImpl implements SocketService {
	@Resource
	private MachineMonitorBackendProperties machineMonitorBackendProperties;
	@Autowired
	private AlarmUtil alarmUtil;
	@Autowired
	private Inno72MachineMapper inno72MachineMapper;

	@Override
	public void updateNetStatus(SystemStatus systemStatus) {
		try {
			String ping = systemStatus.getPing();
			int pingInt = Integer.parseInt(ping.replace("ms", ""));
			Map<String, Object> map = new HashMap<>();
			map.put("machineCode", systemStatus.getMachineId());
			if (pingInt <= 100) {
				map.put("netStatus", 4);
			} else if (pingInt > 100 && pingInt <= 300) {
				map.put("netStatus", 3);
			} else if (pingInt > 300 && pingInt <= 500) {
				map.put("netStatus", 2);
			} else if (pingInt > 500 && pingInt <= 1000) {
				map.put("netStatus", 1);
			} else {
				map.put("netStatus", 0);
			}
			List<Map<String, Object>> list = new ArrayList<>();
			list.add(map);
			String urlProp = machineMonitorBackendProperties.getProps().get("updateMachineListNetStatusUrl");
			HttpClient.post(urlProp, JSON.toJSONString(list));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void recordHeart(String machineCode) {
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> list = inno72MachineMapper.selectByCondition(condition);
		if (list != null && !list.isEmpty()) {
			AlarmDetailBean alarmBean = new AlarmDetailBean();
			alarmBean.setMachineId(list.get(0).getId());
			alarmBean.setType(2);
			alarmUtil.saveAlarmDetail(alarmBean);
		}
	}

}
