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
import com.inno72.system.model.Inno72Function;
import com.inno72.system.service.FunctionService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/function")
public class FunctionController {
	@Resource
	private FunctionService functionService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72Function function) {
		functionService.save(function);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		functionService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Function function) {
		functionService.update(function);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Function> detail(@RequestParam String id) {
		Inno72Function function = functionService.findById(id);
		return ResultGenerator.genSuccessResult(function);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72Function.class);
		List<Inno72Function> list = functionService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
