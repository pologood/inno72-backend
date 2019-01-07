package com.inno72.Interact.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.service.InteractGoodsService;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/goods")
@CrossOrigin
public class InteractGoodsController {
	@Resource
	private InteractGoodsService interactGoodsService;

	@RequestMapping(value = "/addCoupon", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody InteractGoodsVo interactGoods) {
		return interactGoodsService.saveCoupon(interactGoods);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(String interactId, String goodsId) {
		return interactGoodsService.deleteById(interactId, goodsId);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateCoupon(@RequestBody InteractGoodsVo interactGoods) {
		return interactGoodsService.updateCoupon(interactGoods);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<InteractGoodsVo> detail(@RequestParam String id, @RequestParam Integer type) {
		InteractGoodsVo interactGoods = interactGoodsService.findGoodsById(id, type);
		return ResultGenerator.genSuccessResult(interactGoods);
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<InteractGoodsVo>> getList(String interactId, String shopsId,
			@RequestParam(required = false) Integer isAlone) {
		List<InteractGoodsVo> list = interactGoodsService.getList(interactId, shopsId, isAlone);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/getToAddList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> getToAddList(String interactId, String shopsId, String sellerId) {
		List<Map<String, Object>> list = interactGoodsService.getToAddList(interactId, shopsId, sellerId);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72InteractGoods.class);
		List<Inno72InteractGoods> list = interactGoodsService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/couponGetList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> couponGetList(String interactId, String shopsId) {
		List<Map<String, Object>> list = interactGoodsService.couponGetList(interactId, shopsId);
		return ResultGenerator.genSuccessResult(list);
	}
}
