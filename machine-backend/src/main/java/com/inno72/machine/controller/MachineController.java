package com.inno72.machine.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineExceptionVo;
import com.inno72.machine.vo.MachineListVo;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachinePortalVo;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.MachineStockOutInfo;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.machine.vo.UpdateMachineVo;

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
	 * 查询机器状态是正常的机器列表
	 *
	 * @param machineStatus
	 * @return
	 */
	@RequestMapping(value = "/findMachineByMachineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<String>> findMachineByMachineStatus(@RequestParam int machineStatus) {
		return machineService.findMachineByMachineStatus(machineStatus);

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

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", "abcde");
		map.put("machineCode", "1234");
		map.put("local", "北京市朝阳区大悦城一层");
		map.put("offline_time", "2018.08.03 12:02:30");
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> mm = new HashMap<>();
		mm.put("goodName", "娃哈哈");
		mm.put("goodCount", 10);
		Map<String, Object> mm1 = new HashMap<>();
		mm1.put("goodName", "娃哇哇");
		mm1.put("goodCount", 5);
		list.add(mm);
		list.add(mm1);
		map.put("stockoutInfo", list);
		map.put("machineDoorStatus", 1);
		map.put("dropGoodsSwitch", 1);
		map.put("temperature", 36);
		map.put("screenIntensity", 10);
		map.put("goodsChannelStatus", "");
		map.put("voice", 10);
		map.put("update_time", "2018.08.03 12:02:30");
		System.out.println(JSON.toJSONString(Results.success(map)));
	}

	/**
	 * 查看机器排期计划
	 *
	 * @param machineCode
	 * @param localCode
	 * @return
	 */
	@RequestMapping(value = "/planList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineListVo>> planList(@RequestParam(required = false) String machineCode,
			@RequestParam(required = false) String localCode, @RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime) {
		List<MachineListVo> list = machineService.findMachinePlan(machineCode, localCode, startTime, endTime);
		return ResultGenerator.genSuccessResult(list);
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
	 * 更新机器
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/updateMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachine(UpdateMachineVo vo) {
		return machineService.updateMachine(vo);

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

	/**
	 * 获取机器首页数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findMachinePortalData", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachinePortalVo> findMachinePortalData() {
		return machineService.findMachinePortalData();
	}

	/**
	 * 查询异常机器列表
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/findExceptionMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineExceptionVo>> findExceptionMachine(@RequestParam Integer type) {
		return machineService.findExceptionMachine(type);
	}

	/**
	 * 查询机器缺货详情
	 * 
	 * @param machineId
	 * @return
	 */
	@RequestMapping(value = "/findMachineStockoutInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineStockOutInfo>> findMachineStockoutInfo(@RequestParam String machineId) {
		return machineService.findMachineStockoutInfo(machineId);
	}

	/**
	 * 修改机器编号
	 * 
	 * @param machineId
	 * @param machineCode
	 * @return
	 */
	@RequestMapping(value = "/updateMachineCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineCode(@RequestParam String machineId, @RequestParam String machineCode) {
		return machineService.updateMachineCode(machineId, machineCode);

	}
}
