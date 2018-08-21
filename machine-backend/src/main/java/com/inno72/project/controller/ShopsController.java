package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.ShopsService;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@RestController
@RequestMapping("/project/shops")
@CrossOrigin
public class ShopsController {
	@Resource
	private ShopsService shopsService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@Valid Inno72Shops shops, BindingResult bindingResult) {

		try {
			if (bindingResult.hasErrors()) {
				return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
			} else {
				return shopsService.saveModel(shops);
			}
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}

		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		try {
			return shopsService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Shops shops) {

		try {
			return shopsService.updateModel(shops);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}

	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Shops> detail(@RequestParam String id) {
		Inno72Shops shops = shopsService.findById(id);
		return ResultGenerator.genSuccessResult(shops);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String code,
			@RequestParam(required = false) String keyword) {
		List<Inno72Shops> list = shopsService.findByPage(code, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Shops>> getList(Inno72Shops shops) {
		List<Inno72Shops> list = shopsService.getList(shops);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/selectActivityShops", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Shops>> selectActivityShops(@RequestParam(required = false) String activityId,
			@RequestParam(required = false) String keyword) {
		List<Inno72Shops> list = shopsService.selectActivityShops(activityId, keyword);
		return ResultGenerator.genSuccessResult(list);
	}
}
