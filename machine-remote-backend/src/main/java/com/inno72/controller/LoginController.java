package com.inno72.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.util.MapUtil;

@RestController
public class LoginController {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> login(@RequestBody(required = false) Map<String, Object> param) {
		String userName = MapUtil.getParam(param, "userName", String.class);
		String password = MapUtil.getParam(param, "password", String.class);
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
	public Result<List<Map<String, Object>>> machineList(@RequestBody(required = false) Map<String, Object> param) {
		String machineCode = MapUtil.getParam(param, "machineCode", String.class);
		String machinKey = CommonConstants.REDIS_SESSION_PATH + (machineCode == null ? "" : machineCode) + "*";
		Set<String> keys = stringRedisTemplate.keys(machinKey);
		if (keys == null || keys.isEmpty()) {
			return Results.success();
		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (String key : keys) {
			String m = key.substring(16);
			Map<String, Object> map = new HashMap<>();
			map.put("machineCode", m);
			list.add(map);
		}
		return Results.success(list);
	}

}
