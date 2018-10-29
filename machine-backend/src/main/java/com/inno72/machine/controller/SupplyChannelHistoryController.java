package com.inno72.machine.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;

@CrossOrigin
@RestController
@RequestMapping("/supply/channel/history")
public class SupplyChannelHistoryController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SupplyChannelHistoryService supplyChannelHistoryService;

	@RequestMapping(value = "/list")
	public ModelAndView findListByPage(@RequestBody SupplyOrderVo supplyOrderVo) {
		List<SupplyOrderVo> list = supplyChannelHistoryService.findListByPage(supplyOrderVo);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 补货详情
	 */
	@RequestMapping(value = "/detail", method = { RequestMethod.GET })
	public Result<List<Inno72SupplyChannelHistory>> workOrderDetail(SupplyRequestVo vo) {
		logger.info("查询补货记录详情接口参数：{}", JSON.toJSON(vo));
		return supplyChannelHistoryService.detail(vo);
	}

	/**
	 * 商品统计
	 */
	@RequestMapping(value = "/dayGoodsCount")
	public ModelAndView dateGoodsCount(SupplyOrderVo supplyOrderVo) {
		List<Map<String, Object>> list = supplyChannelHistoryService.dayGoodsCount(supplyOrderVo);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 新——补货记录
	 */
	@RequestMapping(value = "/dayGoodsList")
	public ModelAndView dateGoodsList(SupplyOrderVo supplyOrderVo) {
		List<Map<String, Object>> list = supplyChannelHistoryService.dayGoodsList(supplyOrderVo);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 新——补货记录详情
	 */
	@RequestMapping(value = "/dayGoodsDetail", method = { RequestMethod.GET })
	public Result<List<Map<String, Object>>> dateGoodsDetail(String machineId, String dateTime) {
		List<Map<String, Object>> list = supplyChannelHistoryService.dayGoodsDetail(machineId, dateTime);
		return ResultGenerator.genSuccessResult(list);
	}
}
