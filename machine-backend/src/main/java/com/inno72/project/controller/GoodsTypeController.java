package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72GoodsType;
import com.inno72.project.service.GoodsTypeService;

/**
 * Created by CodeGenerator on 2018/12/29.
 */
@RestController
@RequestMapping("/goods/type")
@CrossOrigin
public class GoodsTypeController {
	@Resource
	private GoodsTypeService goodsTypeService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72GoodsType goodsType) {
		return goodsTypeService.saveModel(goodsType);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		goodsTypeService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72GoodsType goodsType) {
		goodsTypeService.updateModel(goodsType);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72GoodsType> detail(@RequestParam String id) {
		Inno72GoodsType goodsType = goodsTypeService.findById(id);
		return ResultGenerator.genSuccessResult(goodsType);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String code, String keyword) {
		List<Inno72GoodsType> list = goodsTypeService.findByPage(code, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72GoodsType>> getList(String code, String keyword) {
		List<Inno72GoodsType> list = goodsTypeService.findByPage(code, keyword);
		return ResultGenerator.genSuccessResult(list);
	}
}
