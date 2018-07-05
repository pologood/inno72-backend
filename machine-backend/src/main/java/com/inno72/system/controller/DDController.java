package com.inno72.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.system.service.DDService;

@RestController
@RequestMapping("/dd")
public class DDController {

	@Autowired
	private DDService ddService;

	@RequestMapping("")
	@ResponseBody
	public String userCallBack(@RequestBody String data, String signature, String timestamp, String nonce) {
		String result = ddService.process(data, signature, timestamp, nonce);
		return result;
	}

	@RequestMapping("/login")
	public Result<String> login(String code, String state) {
		return ddService.login(code, state);
	}

	@RequestMapping(value = "/token", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> token() {
		return ddService.getToken();
	}

	@RequestMapping("/reg_call_back")
	@ResponseBody
	public Result<String> registryCallback(String url) {
		return ddService.registryCallback(url);
	}

	@RequestMapping("/update_call_back")
	@ResponseBody
	public Result<String> updateRegistryCallback(String url) {
		return ddService.updateRegistryCallback(url);
	}

	@RequestMapping(value = "/initDData", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> initDData() {
		return ddService.initDData();
	}
}
