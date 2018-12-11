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

import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.service.InteractShopsService;
import com.inno72.Interact.vo.InteractShopsVo;
import com.inno72.Interact.vo.ShopsVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/shops")
@CrossOrigin
public class InteractShopsController {
	@Resource
	private InteractShopsService interactShopsService;

	@RequestMapping(value = "/checkShops", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> checkShops(String interactId, String sellerId) {
		List<Map<String, Object>> list = interactShopsService.checkShops(interactId, sellerId);

		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody InteractShopsVo interactShops) {
		return interactShopsService.save(interactShops);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(String interactId, String shopsId, Integer isVip) {
		return interactShopsService.update(interactId, shopsId, isVip);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(String interactId, String shopsId) {
		return interactShopsService.deleteById(interactId, shopsId);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<ShopsVo> detail(@RequestParam String id, String interactId) {
		ShopsVo interactShopsVo = interactShopsService.findShopsById(id, interactId);
		return ResultGenerator.genSuccessResult(interactShopsVo);
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<ShopsVo>> getList(String interactId, String merchantId) {
		List<ShopsVo> list = interactShopsService.getList(interactId, merchantId);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72InteractShops.class);
		List<Inno72InteractShops> list = interactShopsService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
