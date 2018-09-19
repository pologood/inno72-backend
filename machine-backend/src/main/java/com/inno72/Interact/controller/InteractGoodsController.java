package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.service.InteractGoodsService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/goods")
public class InteractGoodsController {
	@Resource
	private InteractGoodsService interactGoodsService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72InteractGoods interactGoods) {
		interactGoodsService.save(interactGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		interactGoodsService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72InteractGoods interactGoods) {
		interactGoodsService.update(interactGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72InteractGoods> detail(@RequestParam String id) {
		Inno72InteractGoods interactGoods = interactGoodsService.findById(id);
		return ResultGenerator.genSuccessResult(interactGoods);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72InteractGoods.class);
		List<Inno72InteractGoods> list = interactGoodsService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
