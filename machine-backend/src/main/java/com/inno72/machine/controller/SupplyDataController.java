package com.inno72.machine.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72CheckGoodsDetail;
import com.inno72.machine.model.Inno72CheckGoodsNum;
import com.inno72.machine.service.SupplyDataService;
import com.inno72.machine.vo.SupplyDataVo;

@CrossOrigin
@RestController
@RequestMapping("/supply/data")
public class SupplyDataController {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SupplyDataService supplyDataService;

	@RequestMapping(value = "/list")
	public ModelAndView list(SupplyDataVo supplyDataVo){
		logger.info("获取补货统计数据接收参数：{}", JSON.toJSON(supplyDataVo));
		List<Inno72CheckGoodsNum> list = supplyDataService.findListByPage(supplyDataVo);
		logger.info("获取补货统计数据返回数据：{}", JSON.toJSON(list));
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "detail")
	public Result<List<Inno72CheckGoodsDetail>> detail(String id){
		logger.info("获取补货统计数据详情参数：{}", JSON.toJSON(id));
		Result<List<Inno72CheckGoodsDetail>> result = supplyDataService.findDetail(id);
		logger.info("获取补货统计数据详情返回数据：{}", JSON.toJSON(result));
		return result;
	}





}
