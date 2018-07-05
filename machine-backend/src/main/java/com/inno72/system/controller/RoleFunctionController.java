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
import com.inno72.system.model.Inno72RoleFunction;
import com.inno72.system.service.RoleFunctionService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/role/function")
public class RoleFunctionController {
	@Resource
	private RoleFunctionService roleFunctionService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72RoleFunction roleFunction) {
		roleFunctionService.save(roleFunction);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		roleFunctionService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72RoleFunction roleFunction) {
		roleFunctionService.update(roleFunction);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72RoleFunction> detail(@RequestParam String id) {
		Inno72RoleFunction roleFunction = roleFunctionService.findById(id);
		return ResultGenerator.genSuccessResult(roleFunction);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72RoleFunction.class);
		List<Inno72RoleFunction> list = roleFunctionService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
