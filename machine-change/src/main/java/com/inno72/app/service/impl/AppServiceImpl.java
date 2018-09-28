package com.inno72.app.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.app.mapper.Inno72AppMapper;
import com.inno72.app.model.Inno72App;
import com.inno72.app.model.Inno72Machine;
import com.inno72.app.service.AppService;
import com.inno72.app.service.MachineService;
import com.inno72.common.AbstractService;
import com.inno72.common.MachineChangeProperties;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.util.LogType;
import com.inno72.common.util.LogUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.AppStatus;
import com.inno72.model.MachineAppStatus;
import com.inno72.model.MachineStartAppBean;
import com.inno72.model.SendMessageBean;
import com.inno72.mongo.MongoUtil;
import com.inno72.plugin.http.HttpClient;

@Service
@Transactional
public class AppServiceImpl extends AbstractService<Inno72App> implements AppService {

	@Resource
	private Inno72AppMapper inno72AppMapper;

	@Resource
	private MachineService machineService;

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private MachineChangeProperties machineChangeProperties;
	@Override
	public List<Inno72App> findByPage(Object condition) {
		return null;
	}

	@Override
	public Result<Map<String,Object>> getAppList(String machineId) {
		Map<String,Object> map = new HashMap<>();
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machineId));
		List<Inno72App> appList = null;
		MachineAppStatus machineAppStatus = mongoTpl.findOne(query,MachineAppStatus.class,"MachineAppStatus");
		if(machineAppStatus != null){
			List<AppStatus>  appStatusList = machineAppStatus.getStatus();
			if(appStatusList != null && appStatusList.size()>0){
				String[] appPackegeNames = new String[appStatusList.size()];
				int j=0;
				for(AppStatus appStatus:appStatusList){
					int versionCode = appStatus.getVersionCode();
					if(versionCode != -1){
						appPackegeNames[j] = appStatus.getAppPackageName();
						j++;
					}
				}
				if(appPackegeNames.length>0){
					appList = inno72AppMapper.selectList(appPackegeNames);
				}
			}
		}
		List<Inno72App> resultList = new ArrayList<>();
		if(appList != null && appList.size()>0){
			for(Inno72App app:appList){
				String packageName = app.getAppPackageName();
				if(!packageName.equals("com.inno72.app")){
					resultList.add(app);
				}
			}
		}
		Inno72Machine machine = machineService.getMachineByCode(machineId);
		int netStatus = 0;
		if(machine != null){
			netStatus = machine.getNetStatus();
		}
		map.put("netStatus",netStatus);
		map.put("resultList",resultList);
		return ResultGenerator.genSuccessResult(map);
	}

	@Override
	public Result<String> changeApp(String machineId, String appPackageName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machineId));
		MachineAppStatus machineAppStatus = mongoTpl.findOne(query,MachineAppStatus.class,"MachineAppStatus");
		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(2);
		msg.setSubEventType(1);
		msg.setMachineId(machineId);
		String name1 = "未知";
		String version1 = "未知版本";
		String name2 = "未知";
		String version2 = "未知版本";
		List<MachineStartAppBean> sl = new ArrayList<>();
		for (AppStatus status : machineAppStatus.getStatus()) {
			if (status.getAppStatus() == 1) {
				name1 = status.getAppName();
				version1 = status.getVersionName();
			}

			MachineStartAppBean bean = new MachineStartAppBean();
			bean.setAppPackageName(status.getAppPackageName());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("appPackageName",status.getAppPackageName());
			Inno72App app = inno72AppMapper.findOneByParam(map);
			bean.setAppType(app.getAppType());
			if (appPackageName.equals(status.getAppPackageName()) || app.getAppType() == 1) {
				bean.setStartStatus(1);
				name2 = status.getAppName();
				version2 = status.getVersionName();
			} else {
				bean.setStartStatus(2);
			}
			sl.add(bean);
		}
		msg.setData(sl);
		LogUtil.logger(LogType.CUT_APP.getCode(), machineId, "用户将运行的APP由"+name1+version1+"切换为"+name2+version2);

		return sendMsg(machineId,msg);
	}

	private Result<String> sendMsg(String machineCode, SendMessageBean... beans) {
		String url = machineChangeProperties.getProps().get("sendAppMsgUrl");
		try {
			String result = HttpClient.post(url, JSON.toJSONString(beans));
			if (!StringUtil.isEmpty(result)) {
				JSONObject $_result = JSON.parseObject(result);
				if ($_result.getInteger("code") == 0) {
					String r = $_result.getJSONObject("data").getString(machineCode);
					if (!"发送成功".equals(r)) {
						return Results.failure("切换APP失败，请检查机器网络");
					}
				} else {
					return Results.failure("切换APP失败，请检查机器网络");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("切换APP失败，请检查机器网络");
		}
		return Results.success();
	}
}
