package com.inno72.system.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.service.UserFunctionDataService;
import com.inno72.system.vo.FunctionTreeResultVo;
import com.inno72.system.vo.UserAreaDataVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/data")
public class UserFunctionDataController {
	@Resource
	private UserFunctionDataService userFunctionDataService;

	@RequestMapping(value = "/updateFunctionData", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody UserAreaDataVo userData) {
		userFunctionDataService.updateFunctionData(userData);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<FunctionTreeResultVo> all(@RequestParam(required = false) String userId) {
		return userFunctionDataService.findAllTree(userId);
	}
}
