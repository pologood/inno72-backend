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
import com.inno72.system.model.Inno72UserRole;
import com.inno72.system.service.UserRoleService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@RestController
@RequestMapping("/user/role")
public class UserRoleController {
	@Resource
	private UserRoleService userRoleService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72UserRole userRole) {
		userRoleService.save(userRole);
		return ResultGenerator.genSuccessResult();

	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		userRoleService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72UserRole userRole) {
		userRoleService.update(userRole);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72UserRole> detail(@RequestParam String id) {
		Inno72UserRole userRole = userRoleService.findById(id);
		return ResultGenerator.genSuccessResult(userRole);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72UserRole.class);
		List<Inno72UserRole> list = userRoleService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
