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
import com.inno72.store.model.Inno72StoreGoodsDetail;
import com.inno72.store.service.StoreGoodsDetailService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2019/01/16.
 */
@RestController
@RequestMapping("/store/goods/detail")
public class StoreGoodsDetailController {
	@Resource
	private StoreGoodsDetailService storeGoodsDetailService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72StoreGoodsDetail storeGoodsDetail) {
		storeGoodsDetailService.save(storeGoodsDetail);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		storeGoodsDetailService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72StoreGoodsDetail storeGoodsDetail) {
		storeGoodsDetailService.update(storeGoodsDetail);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72StoreGoodsDetail> detail(@RequestParam String id) {
		Inno72StoreGoodsDetail storeGoodsDetail = storeGoodsDetailService.findById(id);
		return ResultGenerator.genSuccessResult(storeGoodsDetail);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72StoreGoodsDetail.class);
		List<Inno72StoreGoodsDetail> list = storeGoodsDetailService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
