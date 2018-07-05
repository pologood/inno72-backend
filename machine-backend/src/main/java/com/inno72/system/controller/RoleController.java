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
import com.inno72.system.model.Inno72Role;
import com.inno72.system.service.RoleService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/role")
public class RoleController {
	@Resource
	private RoleService roleService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72Role role) {
		roleService.save(role);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		roleService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Role role) {
		roleService.update(role);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Role> detail(@RequestParam String id) {
		Inno72Role role = roleService.findById(id);
		return ResultGenerator.genSuccessResult(role);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72Role.class);
		List<Inno72Role> list = roleService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
