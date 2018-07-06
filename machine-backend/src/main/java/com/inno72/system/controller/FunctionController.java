package com.inno72.system.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.service.FunctionService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/system/function")
public class FunctionController {
	@Resource
	private FunctionService functionService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String keyword) {
		Result<List<Inno72Function>> list = functionService.findFunctions(keyword);
		return ResultPages.page(list);
	}

	@RequestMapping(value = "/all", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Function>> all() {
		return Results.success(functionService.findAll());
	}
}
