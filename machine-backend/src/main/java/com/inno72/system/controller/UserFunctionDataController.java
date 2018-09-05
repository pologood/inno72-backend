package com.inno72.system.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.model.Inno72FunctionData;
import com.inno72.system.service.UserFunctionDataService;
import com.inno72.system.vo.FunctionTreeResultVo;
import com.inno72.system.vo.UserAreaDataVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/data")
@CrossOrigin
public class UserFunctionDataController {
	@Resource
	private UserFunctionDataService userFunctionDataService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72FunctionData>> detail(@RequestParam String userId) {
		List<Inno72FunctionData> userFunctionData = userFunctionDataService.list(userId);
		return ResultGenerator.genSuccessResult(userFunctionData);
	}

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
