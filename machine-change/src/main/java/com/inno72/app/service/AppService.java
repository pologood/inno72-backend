package com.inno72.app.service;

import java.util.List;
import java.util.Map;

import com.inno72.app.model.Inno72App;
import com.inno72.common.Result;
import com.inno72.common.Service;

public interface AppService extends Service<Inno72App> {


	public Result<Map<String,Object>> getAppList(String machineId);

	public Result<String> changeApp(String machineId,String appPackegeName);
}
