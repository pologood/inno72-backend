package com.inno72.controller;

import com.inno72.redis.IRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestMsgController {
	@Autowired
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/testMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public void processCustomMsg() {
        redisUtil.publish("moniterAlarm", "msg---------");

	}

}
