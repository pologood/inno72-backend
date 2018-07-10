package com.inno72.machine.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72SupplyChannelHist;
import com.inno72.machine.service.SupplyChannelServiceHist;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@RequestMapping("/machine/channel/out")
public class SupplyChannelOutController {
	@Resource
	private SupplyChannelServiceHist supplyChannelService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72SupplyChannelHist supplyChannel) {
		supplyChannelService.save(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		supplyChannelService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72SupplyChannelHist supplyChannel) {
		supplyChannelService.update(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72SupplyChannelHist> detail(@RequestParam String id) {
		Inno72SupplyChannelHist supplyChannel = supplyChannelService.findById(id);
		return ResultGenerator.genSuccessResult(supplyChannel);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72SupplyChannelHist.class);
		List<Inno72SupplyChannelHist> list = supplyChannelService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 商品数减一
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/subCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72SupplyChannelHist> subCount(@RequestBody Inno72SupplyChannelHist supplyChannel) {
		Result<Inno72SupplyChannelHist> result = supplyChannelService.subCount(supplyChannel);
		return result;
	}

	/**
	 * 获取货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/get", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> getSupplyChannel(@RequestBody Inno72SupplyChannelHist supplyChannel) {
		@SuppressWarnings("unchecked")
		Result<String> result = supplyChannelService.getSupplyChannel(supplyChannel);
		return result;
	}
}
