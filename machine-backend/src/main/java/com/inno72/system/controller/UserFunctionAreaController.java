package com.inno72.system.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.service.UserFunctionAreaService;
import com.inno72.system.vo.AreaTreeResultVo;
import com.inno72.system.vo.UserAreaDataVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/area")
public class UserFunctionAreaController {
	@Resource
	private UserFunctionAreaService userFunctionAreaService;

	@RequestMapping(value = "/updateFunctionArea", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateFunctionArea(@RequestBody UserAreaDataVo userArea) {
		userFunctionAreaService.updateFunctionArea(userArea);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<AreaTreeResultVo> all(@RequestParam(required = false) String userId) {
		return userFunctionAreaService.findAllAreaTree(userId);
	}

}