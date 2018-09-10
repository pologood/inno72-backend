package com.inno72.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;

@RestController
public class LoginController {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> login(@RequestParam String userName, @RequestParam String password) {
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			return Results.failure("用户名或密码为空");
		}
		if (!"nb".equals(userName) || !"nb".equals(password)) {
			return Results.failure("用户名或密码错误");
		}
		String machinKey = CommonConstants.REDIS_SESSION_PATH + "*";
		Set<String> keys = stringRedisTemplate.keys(machinKey);
		if (keys == null || keys.isEmpty()) {
			return Results.failure("当前没有在线机器");
		}

		return Results.success();
	}

	@RequestMapping(value = "/machineList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<String>> machineList(@RequestParam(defaultValue = "") String machineCode) {
		String machinKey = CommonConstants.REDIS_SESSION_PATH + machineCode + "*";
		Set<String> keys = stringRedisTemplate.keys(machinKey);
		if (keys == null || keys.isEmpty()) {
			return Results.success();
		}
		List<String> list = new ArrayList<>();
		for (String key : keys) {
			String m = key.substring(16);
			list.add(m);
		}
		return Results.success(list);
	}

}
