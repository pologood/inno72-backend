package com.inno72.app.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSON;
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
		if (StringUtil.isEmpty(deviceId)) {
			return Results.failure("deviceId传入为空");
		}
		return machineService.generateMachineId(deviceId);
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

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("deviceId", "aaa111");
		map.put("age", "12");
		System.out.println(AesUtils.encrypt(JSON.toJSONString(map)));
	}

}