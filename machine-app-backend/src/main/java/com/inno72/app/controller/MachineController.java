package com.inno72.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.app.model.Inno72MachineBatch;
import com.inno72.app.service.MachineService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.util.AesUtils;

@RestController
@RequestMapping("/machine")
@CrossOrigin
public class MachineController {
	@Resource
	private MachineService machineService;

	/**
	 * 生成机器id
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/generateMachineId", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> generateMachineId(@RequestBody Map<String, Object> msg) {
		String deviceId = (String) Optional.of(msg).map(a -> a.get("deviceId")).orElse("");
		String batcId = (String) Optional.of(msg).map(a -> a.get("batcId")).orElse("18");

		if (StringUtil.isEmpty(deviceId)) {
			return Results.failure("deviceId传入为空");
		}
		return machineService.generateMachineId(deviceId, batcId);
	}

	/**
	 * 获取机器批次
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/getMachineBatchs", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72MachineBatch>> getMachineBatchs() {
		return machineService.getMachineBatchs();
	}

	/**
	 * 初始化机器
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/initMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> initMachine(@RequestBody Map<String, Object> msg) {
		return machineService.initMachine(msg);
	}

	/**
	 * 更新机器状态
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateMachineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineStatus(@RequestBody Map<String, Object> msg) {
		return machineService.updateMachineStatus(msg);
	}

	/**
	 * 拆分合并货道
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/setMachineChannel", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> setMachineChannel(@RequestBody Map<String, Object> msg) {
		return machineService.setMachineChannel(msg);
	}

	/**
	 * 获取机器信息
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/getMachineStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Integer> getMachineStatus(@RequestBody Map<String, Object> msg) {
		return machineService.getMachineStatus(msg);
	}

	/**
	 * 更新机器code
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateMachineCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineCode(@RequestBody Map<String, Object> msg) {
		return machineService.updateMachineCode(msg);
	}

	/**
	 * 获取机器点位信息
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/getMachineLocale", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> getMachineLocale(@RequestBody Map<String, Object> msg) {
		return machineService.getMachineLocale(msg);
	}

	/**
	 * 获取所有货道
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/getMachineChannels", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> getMachineChannels(@RequestBody Map<String, Object> msg) {
		return machineService.getMachineChannels(msg);
	}

	/**
	 * 同步货道
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateMachineChannels", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateMachineChannels(@RequestBody Map<String, Object> msg) {
		return machineService.updateMachineChannels(msg);
	}

	/**
	 * 设置机器为天猫合作机器
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/resetTuMachine", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> resetTuMachine(@RequestBody Map<String, Object> msg) {
		return machineService.resetTuMachine(msg);
	}

	/**
	 * 更新机器wifi密码
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateWifiPwd", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateWifiPwd(@RequestBody Map<String, Object> msg) {
		return machineService.updateWifiPwd(msg);
	}

	/**
	 * 启用货道
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateChannelStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateChannelStatus(@RequestBody Map<String, Object> msg) {
		return machineService.updateChannelStatus(msg);
	}

	/**
	 * 查询服务器时间
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/getServerTime", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> getServerTime(@RequestBody(required = false) Map<String, Object> msg) {
		return machineService.getServerTime(msg);
	}

	@PostMapping("encrypt")
	public String encrypt(HttpServletRequest request) throws IOException {
		byte[] encryptRequestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
		String encryptRequestBody = new String(encryptRequestBodyBytes);
		return AesUtils.encrypt(encryptRequestBody);
	}

	@PostMapping("decrypt")
	public String decrypt(HttpServletRequest request) throws IOException {
		byte[] encryptRequestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
		String encryptRequestBody = new String(encryptRequestBodyBytes);
		return AesUtils.decrypt(encryptRequestBody);
	}

}