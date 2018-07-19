package com.inno72.machine.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.UpdateMachineChannelVo;

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
	 * @return
	 */
	@RequestMapping(value = "/updateMachineListNetStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineNetInfo>> updateMachineListNetStatus(@RequestBody List<MachineNetInfo> list) {
		return machineService.updateMachineListNetStatus(list);

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
	public Result<String> deleteChannel(@RequestBody List<UpdateMachineChannelVo> channels) {
		return machineService.deleteChannel(channels);

	}

	/**
	 * 更新商品余量
	 *
	 * @param channelId
	 * @param count
	 * @return
	 */
	@RequestMapping(value = "/updateGoodsCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateGoodsCount(@RequestBody List<UpdateMachineChannelVo> channels) {
		return machineService.updateGoodsCount(channels);

	}

	/**
	 * 查询机器状态
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/machineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachineStatusVo> machineStatus(@RequestParam String machineId) {
		return machineService.machineStatus(machineId);

	}

	/**
	 * 查询app状态
	 *
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/appStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachineAppStatus> appStatus(@RequestParam String machineId) {
		return machineService.appStatus(machineId);

	}

	/**
	 * 更新数据
	 *
	 * @param machineId
	 * @param updateStatus
	 * @return
	 */
	@RequestMapping(value = "/updateInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateInfo(@RequestParam String machineId, @RequestParam Integer updateStatus) {
		return machineService.updateInfo(machineId, updateStatus);

	}

	/**
	 * 切换app
	 * 
	 * @param machineId
	 * @param appPackageName
	 * @return
	 */
	@RequestMapping(value = "/cutApp", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> cutApp(@RequestParam String machineId, @RequestParam String appPackageName) {
		return machineService.cutApp(machineId, appPackageName);

	}

	/**
	 * 安装app
	 * 
	 * @param machineId
	 * @param appPackageName
	 * @param url
	 * @param versionCode
	 * @return
	 */
	@RequestMapping(value = "/installApp", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> installApp(@RequestParam String machineId, @RequestParam String appPackageName,
			@RequestParam String url, @RequestParam Integer versionCode) {
		return machineService.installApp(machineId, appPackageName, url, versionCode);

	}

}
