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

	/**
	 * 初始化机器id
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/initMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> initMachine(@RequestParam String deviceId) {
		return machineService.initMachine(deviceId);

	}

	/**
	 * 更新机器网络状态
	 * 
	 * @param machineCode
	 * @param netStatus
	 * @return
	 */
	@RequestMapping(value = "/updateNetStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateNetStatus(@RequestParam String machineCode, @RequestParam Integer netStatus) {
		return machineService.updateNetStatus(machineCode, netStatus);

	}

}
