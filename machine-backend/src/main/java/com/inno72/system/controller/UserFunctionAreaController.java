package com.inno72.system.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.system.service.UserFunctionAreaService;
import com.inno72.system.vo.AreaTreeResultVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/area")
public class UserFunctionAreaController {
	@Resource
	private UserFunctionAreaService userFunctionAreaService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72UserFunctionArea userFunctionArea) {
		userFunctionAreaService.save(userFunctionArea);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72UserFunctionArea userFunctionArea) {
		userFunctionAreaService.update(userFunctionArea);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72UserFunctionArea> detail(@RequestParam String id) {
		Inno72UserFunctionArea userFunctionArea = userFunctionAreaService.findById(id);
		return ResultGenerator.genSuccessResult(userFunctionArea);
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<AreaTreeResultVo> all(@RequestParam(required = false) String userId) {
		return userFunctionAreaService.findAllAreaTree(userId);
	}

}
