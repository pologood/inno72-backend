package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.MachineService;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@RestController
@RequestMapping("/machine")
public class MachineController {
	@Resource
	private MachineService machineService;

	@RequestMapping(value = "/initMeachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> initMeachine(@RequestParam String deviceId) {
		return machineService.initMeachine(deviceId);

	}
}
