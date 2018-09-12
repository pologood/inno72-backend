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
import com.inno72.system.model.Inno72UserFunctionArea;
import com.inno72.system.service.UserFunctionAreaService;
import com.inno72.system.vo.AreaTreeResultVo;
import com.inno72.system.vo.UserAreaDataVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/area")
@CrossOrigin
public class UserFunctionAreaController {
	@Resource
	private UserFunctionAreaService userFunctionAreaService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72UserFunctionArea>> detail(@RequestParam String userId) {
		List<Inno72UserFunctionArea> userFunctionArea = userFunctionAreaService.list(userId);
		return ResultGenerator.genSuccessResult(userFunctionArea);
	}

	@RequestMapping(value = "/updateFunctionArea", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> updateFunctionArea(@RequestBody UserAreaDataVo userArea) {
		return userFunctionAreaService.updateFunctionArea(userArea);
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<AreaTreeResultVo> all(@RequestParam(required = false) String userId) {
		return userFunctionAreaService.findAllAreaTree(userId);
	}

}
