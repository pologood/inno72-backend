package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.service.InteractShopsService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/shops")
public class InteractShopsController {
	@Resource
	private InteractShopsService interactShopsService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72InteractShops interactShops) {
		interactShopsService.save(interactShops);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		interactShopsService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72InteractShops interactShops) {
		interactShopsService.update(interactShops);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72InteractShops> detail(@RequestParam String id) {
		Inno72InteractShops interactShops = interactShopsService.findById(id);
		return ResultGenerator.genSuccessResult(interactShops);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72InteractShops.class);
		List<Inno72InteractShops> list = interactShopsService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
