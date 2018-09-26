package com.inno72.app.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.app.model.Inno72App;
import com.inno72.app.service.AppService;
import com.inno72.common.Result;

@RestController
@CrossOrigin
@RequestMapping("/app")
public class AppController {

	@Resource
	private AppService appService;
	@RequestMapping(value="get")
	public Result<List<Inno72App>> getAppList(String machineId){
		Result<List<Inno72App>> result = appService.getAppList(machineId);
		return result;
	}

	@RequestMapping(value="change")
	public Result<String> changeApp(String machineId,String appPackageName){
		Result<String> result = appService.changeApp(machineId,appPackageName);
		return result;
	}
}
