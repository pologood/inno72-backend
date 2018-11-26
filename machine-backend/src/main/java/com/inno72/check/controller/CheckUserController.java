package com.inno72.check.controller;

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

import com.inno72.check.model.Inno72CheckUserPhone;
import com.inno72.check.service.CheckUserService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.project.vo.Inno72AdminAreaVo;

/**
 * Created by CodeGenerator on 2018/07/18.
 */
@RestController
@RequestMapping("/check/user")
@CrossOrigin
public class CheckUserController {
	@Resource
	private CheckUserService checkUserService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody Inno72CheckUserVo checkUser) {
		try {
			return checkUserService.saveModel(checkUser);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		try {
			return checkUserService.delById(id);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody Inno72CheckUserVo checkUser) {
		try {
			return checkUserService.updateModel(checkUser);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
	}

	@RequestMapping(value = "/updateStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateStatus(@RequestParam String id, @RequestParam int status) {
		try {
			return checkUserService.updateStatus(id, status);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72CheckUserVo> detail(@RequestParam String id) {
		Inno72CheckUserVo checkUser = checkUserService.findDetail(id);
		return ResultGenerator.genSuccessResult(checkUser);
	}

	@RequestMapping(value = "/getUserMachinDetailList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> getUserMachinDetailList(@RequestParam String id) {
		List<Map<String, Object>> list = checkUserService.getUserMachinDetailList(id);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String keyword) {
		List<Inno72CheckUserVo> list = checkUserService.findByPage(keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/selectPhoneByMachineCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72CheckUserPhone>> selectPhoneByMachineCode(
			@RequestBody Inno72CheckUserPhone inno72CheckUserPhone) {
		List<Inno72CheckUserPhone> list = checkUserService.selectPhoneByMachineCode(inno72CheckUserPhone);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/selectAreaMachines", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72AdminAreaVo>> selectMachines(String code, String level, String machineCode) {

		List<Inno72AdminAreaVo> list = checkUserService.selectAreaMachineList(code, level, machineCode);
		return ResultGenerator.genSuccessResult(list);
	}

}
