package com.inno72.machine.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.service.TaskService;
import com.inno72.machine.vo.Inno72TaskVo;
import com.inno72.project.vo.Inno72AdminAreaVo;

/**
 * Created by CodeGenerator on 2018/08/24.
 */
@RestController
@RequestMapping("/machine/task")
@CrossOrigin
public class TaskController {
	@Resource
	private TaskService taskService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody Inno72TaskVo task) {
		return taskService.saveTsak(task);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody Inno72TaskVo task) {
		return taskService.updateTsak(task);
	}

	@RequestMapping(value = "/updateStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateStatus(@RequestParam String id, Integer status, Integer doType) {
		return taskService.updateStatus(id, status, doType);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72TaskVo> detail(@RequestParam String id) {
		Inno72TaskVo task = taskService.findDetail(id);
		return ResultGenerator.genSuccessResult(task);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		return taskService.delById(id);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String type,
			@RequestParam(required = false) String status) {
		List<Inno72TaskVo> list = taskService.findByPage(type, status);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/selectAreaMachines", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72AdminAreaVo>> selectMachines(String code, String level, String machineCode) {
		List<Inno72AdminAreaVo> list = taskService.selectAreaMachineList(code, level, machineCode);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/selectAppList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> selectAppList() {
		List<Map<String, Object>> list = taskService.selectAppList();
		return ResultGenerator.genSuccessResult(list);
	}
}
