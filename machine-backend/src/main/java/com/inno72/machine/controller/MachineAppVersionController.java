package com.inno72.machine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.machine.service.AppVersionService;
import com.inno72.machine.vo.MachineAppVersionVo;

@RestController
@RequestMapping("/machine/version")
@CrossOrigin
public class MachineAppVersionController {

	@Autowired
	private AppVersionService appVersionService;

	@RequestMapping(value = "/appVersion", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView appVersion(String appPackage, Integer versionCode, String keyword) {
		List<MachineAppVersionVo> list = appVersionService.appVersion(appPackage, versionCode, keyword);
		return ResultPages.page(Results.success(list));
	}

}
