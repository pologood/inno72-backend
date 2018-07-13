package com.inno72.machine.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.inno72.common.Result;
import com.inno72.machine.service.MachineService;

import java.util.List;

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
	public Result<String> initMachine(@RequestParam String deviceId, @RequestParam String channelJson) {
		return machineService.initMachine(deviceId, channelJson);

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
	/**
	 * 更新机器网络状态
	 *
	 * @param list
	 * @param netStatus
	 * @return
	 */
	@RequestMapping(value = "/updateMachineListNetStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<String>> updateMachineListNetStatus(@RequestParam List<String> list, @RequestParam Integer netStatus) {
		return machineService.updateMachineListNetStatus(list, netStatus);

	}


}
