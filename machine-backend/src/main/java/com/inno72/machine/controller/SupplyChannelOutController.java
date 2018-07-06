package com.inno72.machine.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@RequestMapping("/machine/channel/out")
public class SupplyChannelOutController {
	@Resource
	private SupplyChannelService supplyChannelService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72SupplyChannel supplyChannel) {
		supplyChannelService.save(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		supplyChannelService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72SupplyChannel supplyChannel) {
		supplyChannelService.update(supplyChannel);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72SupplyChannel> detail(@RequestParam String id) {
		Inno72SupplyChannel supplyChannel = supplyChannelService.findById(id);
		return ResultGenerator.genSuccessResult(supplyChannel);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72SupplyChannel.class);
		List<Inno72SupplyChannel> list = supplyChannelService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 商品数减一
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/subCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72SupplyChannel> subCount(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result result = supplyChannelService.subCount(supplyChannel);
		return result;
	}

	/**
	 * 获取货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/get", method = { RequestMethod.POST, RequestMethod.GET })
	public Result getSupplyChannel(@RequestBody Inno72SupplyChannel supplyChannel) {
		Result result = supplyChannelService.getSupplyChannel(supplyChannel);
		return result;
	}
}