package com.inno72.machine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.machine.service.TrafficService;
import com.inno72.machine.vo.SystemStatus;

@RestController
@RequestMapping("/machine/traffic")
@CrossOrigin
public class MachineTrafficController {

	@Autowired
	private TrafficService trafficService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String machineCode, Integer allTraffic) {
		List<SystemStatus> list = trafficService.list(machineCode, allTraffic);
		return ResultPages.page(Results.success(list));
	}

}
