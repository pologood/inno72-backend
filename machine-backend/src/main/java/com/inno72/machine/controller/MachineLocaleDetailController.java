package com.inno72.machine.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.service.MachineLocaleDetailService;

/**
 * Created by CodeGenerator on 2018/11/08.
 */
@RestController
@RequestMapping("/machine/locale/detail")
@CrossOrigin
public class MachineLocaleDetailController {
	@Resource
	private MachineLocaleDetailService machineLocaleDetailService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String code,
			@RequestParam(required = false) String keyword) {
		List<Map<String, Object>> list = machineLocaleDetailService.findListByPage(code, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/machineLocaleDetail", method = { RequestMethod.GET })
	public Result<List<Map<String, Object>>> findMachineLocaleDetail(String machineId) {
		List<Map<String, Object>> list = machineLocaleDetailService.findMachineLocaleDetail(machineId);
		return ResultGenerator.genSuccessResult(list);
	}
}
