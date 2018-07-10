package com.inno72.machine.controller;

import java.util.List;
import java.util.Map;

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
import com.inno72.machine.model.Inno72SupplyChannelHist;
import com.inno72.machine.service.SupplyChannelServiceHist;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/machine/channel")
public class SupplyChannelController {
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

	/**
	 * 货道列表
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(Inno72SupplyChannelHist supplyChannel) {
		List<Inno72SupplyChannelHist> list = supplyChannelService.getListForPage(supplyChannel);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 合并货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/merge", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> merge(Inno72SupplyChannelHist supplyChannel) {
		Result<String> result = supplyChannelService.merge(supplyChannel);
		return result;
	}

	/**
	 * 拆分货道
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/split", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> split(Inno72SupplyChannelHist supplyChannel) {
		Result<String> result = supplyChannelService.split(supplyChannel);
		return result;
	}

	/**
	 * 货道清零
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "/clear", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> clear(Inno72SupplyChannelHist supplyChannel) {
		Result<String> result = supplyChannelService.clear(supplyChannel);
		return result;
	}

	/**
	 * 一键下架
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "downAll", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> downAll(Inno72SupplyChannelHist supplyChannel) {
		Result<String> result = supplyChannelService.downAll(supplyChannel);
		return result;
	}

	/**
	 * 查询货道操作历史
	 * 
	 * @param supplyChannel
	 * @return
	 */
	@RequestMapping(value = "history", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> history(Inno72SupplyChannelHist supplyChannel) {
		Result<Map<String, Object>> result = supplyChannelService.history(supplyChannel);
		return result;
	}

}
