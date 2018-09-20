package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AppStatus;
import com.inno72.model.AppVersion;
import com.inno72.model.Inno72AppMsg;
import com.inno72.model.Inno72Machine;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MachineInstallAppBean;
import com.inno72.model.SendMessageBean;
import com.inno72.model.SystemStatus;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppMsgService;
import com.inno72.service.SocketService;
import com.inno72.util.AesUtils;
import com.inno72.util.AlarmUtil;
import com.inno72.util.GZIPUtil;

import tk.mybatis.mapper.entity.Condition;

@Service
public class SocketServiceImpl implements SocketService {
	@Resource
	private MachineMonitorBackendProperties machineMonitorBackendProperties;
	@Autowired
	private AlarmUtil alarmUtil;
	@Autowired
	private Inno72MachineMapper inno72MachineMapper;
	@Autowired
	private MongoOperations mongoTpl;
	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private AppMsgService appMsgService;

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

	@Override
	public void checkApp(MachineAppStatus apps) {
		if (apps != null && apps.getStatus() != null) {
			List<MachineInstallAppBean> il = new ArrayList<>();
			List<AppStatus> apps1 = apps.getStatus();
			for (AppStatus app : apps1) {
				Query query = new Query();
				query.addCriteria(Criteria.where("appPackageName").is(app.getAppPackageName()));
				List<AppVersion> appVersions = mongoTpl.find(query, AppVersion.class, "AppVersion");
				if (appVersions != null && !appVersions.isEmpty()) {
					AppVersion appVersion = appVersions.get(0);
					if (app.getVersionCode() < appVersion.getAppVersionCode()) {
						MachineInstallAppBean bean = new MachineInstallAppBean();
						bean.setAppPackageName(appVersion.getAppPackageName());
						bean.setUrl(appVersion.getDownloadUrl());
						bean.setVersionCode(appVersion.getAppVersionCode());
						bean.setSeq(appVersion.getSeq());
						il.add(bean);
					}
				}
			}
			if (!il.isEmpty()) {
				SendMessageBean msg = new SendMessageBean();
				msg.setEventType(2);
				msg.setSubEventType(2);
				msg.setMachineId(apps.getMachineId());
				msg.setData(il);

				String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(msg)));
				String machinKey = CommonConstants.REDIS_SESSION_PATH + msg.getMachineId();
				String sessionId = redisUtil.get(machinKey);
				if (!com.inno72.common.utils.StringUtil.isEmpty(sessionId)) {
					Inno72AppMsg msg1 = new Inno72AppMsg();
					msg1.setId(StringUtil.uuid());
					msg1.setCreateTime(LocalDateTime.now());
					msg1.setMachineCode(msg.getMachineId());
					msg1.setContent(result);
					msg1.setStatus(0);
					msg1.setMsgType(1);
					appMsgService.save(msg1);
				}
			}
		}

	}

}
