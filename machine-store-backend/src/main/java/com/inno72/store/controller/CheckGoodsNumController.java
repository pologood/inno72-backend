package com.inno72.store.controller;

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
import com.inno72.store.model.Inno72CheckGoodsNum;
import com.inno72.store.service.CheckGoodsNumService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2019/01/14.
 */
@RestController
@RequestMapping("/check/goods/num")
public class CheckGoodsNumController {
	@Resource
	private CheckGoodsNumService checkGoodsNumService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72CheckGoodsNum checkGoodsNum) {
		checkGoodsNumService.save(checkGoodsNum);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		checkGoodsNumService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72CheckGoodsNum checkGoodsNum) {
		checkGoodsNumService.update(checkGoodsNum);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72CheckGoodsNum> detail(@RequestParam String id) {
		Inno72CheckGoodsNum checkGoodsNum = checkGoodsNumService.findById(id);
		return ResultGenerator.genSuccessResult(checkGoodsNum);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72CheckGoodsNum.class);
		List<Inno72CheckGoodsNum> list = checkGoodsNumService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
