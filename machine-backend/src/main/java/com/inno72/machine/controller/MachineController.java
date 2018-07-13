package com.inno72.machine.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import com.inno72.common.Result;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.ChannelListVo;

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


	/**
	 * 查看机器列表
	 *
	 * @param machineCode
	 * @param localCode
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String machineCode,
			@RequestParam(required = false) String localCode) {
		Result<List<Inno72Machine>> list = machineService.findMachines(machineCode, localCode);
		return ResultPages.page(list);
	}

	/**
	 * 更新点位
	 *
	 * @param localeId
	 * @param address
	 * @return
	 */
	@RequestMapping(value = "/updateLocale", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateLocale(@RequestParam String id, @RequestParam String localeId,
			@RequestParam(defaultValue = "") String address) {
		return machineService.updateLocale(id, localeId, address);

	}

	/**
	 * 货道详情
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/channelInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<ChannelListVo>> channelInfo(@RequestParam String machineId) {
		return machineService.channelList(machineId);

	}

	/**
	 * 启用、停用货道（1停用，0请用）
	 *
	 * @param channelId
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/deleteChannel", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> deleteChannel(@RequestParam String channelId, @RequestParam Integer status) {
		return machineService.deleteChannel(channelId, status);

	}

	/**
	 * 更新商品余量
	 *
	 * @param channelId
	 * @param count
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateGoodsCount(@RequestParam String channelId, @RequestParam Integer count) {
		return machineService.updateGoodsCount(channelId, count);

	}

}
