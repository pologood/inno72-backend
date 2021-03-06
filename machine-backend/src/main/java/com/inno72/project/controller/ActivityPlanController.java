package com.inno72.project.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.service.ActivityPlanService;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72NoPlanInfoVo;

/**
 * Created by CodeGenerator on 2018/07/11.
 */
@RestController
@RequestMapping("/project/activityPlan")
@CrossOrigin
public class ActivityPlanController {
	@Resource
	private ActivityPlanService activityPlanService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody Inno72ActivityPlanVo activityPlan) {
		try {
			return activityPlanService.saveActPlan(activityPlan);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id, Integer status) {
		try {
			return activityPlanService.delById(id, status);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody Inno72ActivityPlanVo activityPlan) {
		try {
			return activityPlanService.updateModel(activityPlan);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}

	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72ActivityPlan> detail(@RequestParam String id) {
		Inno72ActivityPlan activityPlan = activityPlanService.findById(id);
		return ResultGenerator.genSuccessResult(activityPlan);
	}

	@RequestMapping(value = "/planMachineDetailList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> planMachineDetailList(@RequestParam String id) {
		List<Map<String, Object>> list = activityPlanService.selectPlanMachinDetailList(id);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72ActivityPlanVo>> list(String code, String status, String type, String startTime,
			String endTime, @RequestParam(required = false) String keyword) {
		List<Inno72ActivityPlanVo> list = activityPlanService.selectPlanList(code, status, type, startTime, endTime,
				keyword);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/selectAreaMachines", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72AdminAreaVo>> selectMachines(String code, String level, String startTime, String endTime,
			String machineCode) {
		// 联动查询
		if (StringUtil.isBlank(startTime) || StringUtil.isBlank(endTime)) {
			return Results.failure("请选择计划时间");
		}
		List<Inno72AdminAreaVo> list = activityPlanService.selectAreaMachineList(code, level, startTime, endTime,
				machineCode);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/selectNoPlanMachines", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72NoPlanInfoVo>> selectNoPlanMachines(@RequestParam String taskTime) {
		// 联动查询
		List<Inno72NoPlanInfoVo> list = activityPlanService.selectNoPlanMachineList(taskTime);
		return ResultGenerator.genSuccessResult(list);
	}

}
