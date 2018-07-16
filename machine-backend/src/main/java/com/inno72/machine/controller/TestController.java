package com.inno72.machine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.redis.IRedisUtil;

@RestController
public class TestController {
	@Autowired
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/testMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public void processCustomMsg() {
		redisUtil.publish("1111", "msg---------");

	}

}
