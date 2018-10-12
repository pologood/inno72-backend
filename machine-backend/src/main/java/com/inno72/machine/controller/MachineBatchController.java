package com.inno72.machine.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72MachineBatch;
import com.inno72.machine.service.MachineBatchService;

@RestController
@RequestMapping("/machine/batch")
@CrossOrigin
public class MachineBatchController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MachineBatchService machineBatchService;
	@RequestMapping(value = "/save")
	public Result<String> saveBatch(@RequestBody Inno72MachineBatch batch){
		logger.info("保存批次接口参数：{}", JSON.toJSON(batch));
		Result<String> result = machineBatchService.saveBatch(batch);
		logger.info("保存批次接口返回结果：{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/list")
	public ModelAndView saveBatch(String keyword){
		logger.info("分页查询批次接口参数：{}", JSON.toJSON(keyword));
		List<Inno72MachineBatch> list= machineBatchService.batchList(keyword);
		logger.info("分页查询批次接口返回结果：{}", JSON.toJSON(list));
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/detail")
	public Result<Inno72MachineBatch> detail(String id){
		logger.info("查询批次详情接口参数：{}", JSON.toJSON(id));
		Result<Inno72MachineBatch> result = machineBatchService.detail(id);
		logger.info("查询批次详情接口返回结果：{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "update")
	public Result<String> updateBatch(@RequestBody Inno72MachineBatch batch){
		logger.info("修改批次接口参数：{}", JSON.toJSON(batch));
		Result<String> result = machineBatchService.updateBatch(batch);
		logger.info("修改批次接口返回结果：{}", JSON.toJSON(result));
		return result;
	}
}
