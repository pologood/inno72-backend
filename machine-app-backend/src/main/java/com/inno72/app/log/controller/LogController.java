package com.inno72.app.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.msg.MsgUtil;

@RestController
public class LogController {

	@Autowired
	private MsgUtil msgUtil;

	@GetMapping("test2")
	public String test2() {
		// String code = "push_monitor_code";
		// Map<String, String> params = new HashMap<>();
		// params.put("asd", "哈哈哈哈哈哈哈哈哈哈或或或或或或或或或");
		// msgUtil.sendPush(code, params, "1111", "test", "要放假了", "傻子才信");
		return "test2";
	}

}
