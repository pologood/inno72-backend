package com.inno72.machine.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.machine.service.MachineService;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@RestController
@RequestMapping("/machine/machine")
@CrossOrigin
public class MachineController {
	@Resource
	private MachineService machineService;

	@RequestMapping(value = "/initMeachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> initMeachine(@RequestParam String deviceId) {
		return machineService.initMeachine(deviceId);

	}

}
