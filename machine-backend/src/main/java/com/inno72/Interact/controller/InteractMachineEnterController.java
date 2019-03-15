package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.service.InteractMachineEnterService;
import com.inno72.Interact.vo.MachineEnterVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

/**
 * Created by CodeGenerator on 2019/03/15.
 */
@RestController
@RequestMapping("/machine/enter")
public class InteractMachineEnterController {
	@Resource
	private InteractMachineEnterService interactMachineEnterService;

	@RequestMapping(value = "/updateBatchEnter", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> updateBatchEnter(String interactId, String enterType) {
		return interactMachineEnterService.updateBatchEnter(interactId, enterType);
	}

	@RequestMapping(value = "/updateEnter", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> updateEnter(String machineId, String enterType) {
		return interactMachineEnterService.updateEnter(machineId, enterType);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String interactId, Integer status, String machineCode) {
		List<MachineEnterVo> list = interactMachineEnterService.findByPage(interactId, status, machineCode);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
