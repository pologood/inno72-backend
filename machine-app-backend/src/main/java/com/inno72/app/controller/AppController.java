package com.inno72.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.app.model.Inno72App;
import com.inno72.app.service.AppService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;

@RestController
@RequestMapping("/app")
@CrossOrigin
public class AppController {
	@Resource
	private AppService appService;

	/**
	 * 生成机器id
	 * 
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "/getAppByBlong", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72App>> getAppByBlong(@RequestBody Map<String, Object> msg) {
		String belong = (String) Optional.of(msg).map(a -> a.get("belong")).orElse("");
		if (StringUtil.isEmpty(belong)) {
			return Results.failure("belong传入为空");
		}
		return appService.getAppByBlong(belong);
	}

}