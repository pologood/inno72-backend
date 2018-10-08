package com.inno72.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.app.model.Inno72App;
import com.inno72.app.service.AppService;
import com.inno72.common.Result;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class AppController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AppService appService;
	@RequestMapping(value="get")
	public Result<Map<String,Object>> getAppList(String machineId){
		logger.info("查询机器运行的APP列表接口{}",machineId);
		Result<Map<String,Object>> result = appService.getAppList(machineId);
		return result;
	}

	@RequestMapping(value="change")
	public Result<String> changeApp(String machineId,String appPackageName){
		logger.info("切换APP接口接收参数：{}",machineId,appPackageName);
		Result<String> result = appService.changeApp(machineId,appPackageName);
		if(result.getCode()==0){
			result.setMsg("发送成功");
		}
		logger.info("切换APP返回H5数据：{}",JSON.toJSON(result));
		return result;
	}
}
