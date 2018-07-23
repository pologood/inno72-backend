package com.inno72.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.app.service.PushService;
import com.inno72.common.Result;

@RestController
@RequestMapping("/push")
@CrossOrigin
public class PushController {
	@Autowired
	private PushService pushService;

	@RequestMapping(value = "/pushMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> pushMsg(@RequestBody Map<String, Object> msg) {
		return pushService.pushMsg(msg);
	}

}
