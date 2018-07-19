package com.inno72.app.log.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.msg.MsgUtil;

@RestController
public class LogController {

	@Autowired
	private MsgUtil msgUtil;

	@GetMapping("test2")
	public String test2(int a) throws InterruptedException {
		String code = "push_monitor_code";
		Map<String, String> params = new HashMap<>();
		params.put("machineCode", "111");
		msgUtil.sendPush(code, params, "machineCode", "项目名-类名", "title", "content");
		msgUtil.sendSMS(code, params, "手机号", "项目名-类名");
		Thread.sleep(a);
		return "test2";
	}

}
