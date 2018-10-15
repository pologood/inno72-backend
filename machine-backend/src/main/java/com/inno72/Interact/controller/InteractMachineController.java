package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.service.InteractMachineService;
import com.inno72.Interact.vo.InteractMachineTime;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/machine")
@CrossOrigin
public class InteractMachineController {
	@Resource
	private InteractMachineService interactMachineService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody InteractMachineTime interactMachineTime) {
		return interactMachineService.save(interactMachineTime);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String interactId, @RequestParam String machineId) {
		return interactMachineService.deleteById(interactId, machineId);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody InteractMachineTime interactMachineTime) {
		return interactMachineService.update(interactMachineTime);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MachineVo> detail(@RequestParam(required = false) String interactId, @RequestParam String machineId) {
		MachineVo detail = interactMachineService.findById(interactId, machineId);
		return ResultGenerator.genSuccessResult(detail);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72InteractMachine.class);
		List<Inno72InteractMachine> list = interactMachineService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineVo>> getList(String keyword, String queryStartTime, String queryEndTime) {
		List<MachineVo> list = interactMachineService.getList(keyword, queryStartTime, queryEndTime);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/getHavingMachines", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MachineVo>> getHavingMachines(String interactId, String keyword) {
		List<MachineVo> list = interactMachineService.getHavingMachines(interactId, keyword);
		return ResultGenerator.genSuccessResult(list);
	}
}
