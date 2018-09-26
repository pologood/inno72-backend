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
import com.inno72.app.service.AppService;
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
	private MongoUtil mongoUtil;

	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private MachineChangeProperties machineChangeProperties;
	@Override
	public List<Inno72App> findByPage(Object condition) {
		return null;
	}

	@Override
	public Result<List<Inno72App>> getAppList(String machineId) {
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

		return ResultGenerator.genSuccessResult(appList);
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
			List<MachineStartAppBean> sl = new ArrayList<>();
			for (AppStatus status : machineAppStatus.getStatus()) {
				MachineStartAppBean bean = new MachineStartAppBean();
				bean.setAppPackageName(status.getAppPackageName());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("appPackageName",status.getAppPackageName());
				Inno72App app = inno72AppMapper.findOneByParam(map);
				bean.setAppType(app.getAppType());
				if (appPackageName.equals(status.getAppPackageName()) || app.getAppType() == 1) {
					bean.setStartStatus(1);
				} else {
					bean.setStartStatus(2);
				}
				sl.add(bean);
			}
			msg.setData(sl);
			LogUtil.logger(LogType.CUT_APP.getCode(), machineId, "切换app，包名：" + appPackageName);

			return sendMsg(machineId,msg);
	}

	private Result<String> sendMsg(String machineCode, SendMessageBean... beans) {
//		String url = "http://pre_test.72solo.com:30130/sendMsgToClient/sendMsg";
//		String url = "http://pre_test.72solo.com:9080/sendMsgToClient/sendMsg";//测试
		String url = "http://api.monitor.inner.inno72.com:9080/sendMsgToClient/sendMsg";//正式
//		String url ="http://api.monitor.inner.inno72.com:9080/sendMsgToClient/sendMsg";//预发
		try {
			String result = HttpClient.post(url, JSON.toJSONString(beans));
			if (!StringUtil.isEmpty(result)) {
				JSONObject $_result = JSON.parseObject(result);
				if ($_result.getInteger("code") == 0) {
					String r = $_result.getJSONObject("data").getString(machineCode);
					if (!"发送成功".equals(r)) {
						return Results.failure(r);
					}
				} else {
					return Results.failure($_result.getString("msg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("发送失败");
		}
		return Results.success();
	}
}
