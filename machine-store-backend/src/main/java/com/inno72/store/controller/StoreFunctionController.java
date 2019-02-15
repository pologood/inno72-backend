package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.store.model.Inno72StoreFunction;
import com.inno72.store.service.StoreFunctionService;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@RestController
@RequestMapping("/store/function")
@CrossOrigin
public class StoreFunctionController {
	@Resource
	private StoreFunctionService storeFunctionService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72StoreFunction storeFunction) {
		storeFunctionService.save(storeFunction);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		storeFunctionService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72StoreFunction storeFunction) {
		storeFunctionService.update(storeFunction);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72StoreFunction> detail(@RequestParam String id) {
		Inno72StoreFunction storeFunction = storeFunctionService.findById(id);
		return ResultGenerator.genSuccessResult(storeFunction);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72StoreFunction>> list() {
		logger.info("获取所有权限接口开始");
		List<Inno72StoreFunction> list = storeFunctionService.findAllFunction();
		logger.info("获取所有权限接口返回数据，{}", JSON.toJSON(list));
		return ResultGenerator.genSuccessResult(list);
	}
}
