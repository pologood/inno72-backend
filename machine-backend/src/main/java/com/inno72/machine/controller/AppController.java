package com.inno72.machine.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.service.AppService;
import com.inno72.machine.vo.AppVersionHistory;

@RestController
@RequestMapping("/machine/app")
@CrossOrigin
public class AppController {

	@Autowired
	private AppService appService;

	@RequestMapping(value = "/appList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72App>> appList() {
		List<Inno72App> list = appService.findAll();
		return Results.success(list);
	}

	@RequestMapping(value = "/appVersionList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<AppVersionHistory>> appVersionList(String appPackageName, String keyword) {
		Result<List<AppVersionHistory>> list = appService.findAppVersionList(appPackageName, keyword);
		return list;
	}

	@RequestMapping(value = "/saveHistory")
	public Result<String> saveHistory(@Valid AppVersionHistory history, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
			} else {
				return appService.saveHistory(history);
			}
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}
}
