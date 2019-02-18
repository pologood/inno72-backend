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
import com.inno72.store.model.Inno72CheckGoodsDetail;
import com.inno72.store.service.CheckGoodsDetailService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2019/01/14.
 */
@RestController
@RequestMapping("/check/goods/detail")
public class CheckGoodsDetailController {
	@Resource
	private CheckGoodsDetailService checkGoodsDetailService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72CheckGoodsDetail checkGoodsDetail) {
		checkGoodsDetailService.save(checkGoodsDetail);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		checkGoodsDetailService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72CheckGoodsDetail checkGoodsDetail) {
		checkGoodsDetailService.update(checkGoodsDetail);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72CheckGoodsDetail> detail(@RequestParam String id) {
		Inno72CheckGoodsDetail checkGoodsDetail = checkGoodsDetailService.findById(id);
		return ResultGenerator.genSuccessResult(checkGoodsDetail);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72CheckGoodsDetail.class);
		List<Inno72CheckGoodsDetail> list = checkGoodsDetailService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
