package com.inno72.system.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.service.UserFunctionDataService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@RestController
@RequestMapping("/user/function/data")
public class UserFunctionDataController {
	@Resource
	private UserFunctionDataService userFunctionDataService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72UserFunctionData userFunctionData) {
		userFunctionDataService.save(userFunctionData);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		userFunctionDataService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72UserFunctionData userFunctionData) {
		userFunctionDataService.update(userFunctionData);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72UserFunctionData> detail(@RequestParam String id) {
		Inno72UserFunctionData userFunctionData = userFunctionDataService.findById(id);
		return ResultGenerator.genSuccessResult(userFunctionData);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72UserFunctionData.class);
		List<Inno72UserFunctionData> list = userFunctionDataService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
