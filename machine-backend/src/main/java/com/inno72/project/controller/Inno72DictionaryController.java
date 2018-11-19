package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.project.model.Inno72Dictionary;
import com.inno72.project.service.Inno72DictionaryService;

/**
 * Created by CodeGenerator on 2018/11/16.
 */
@RestController
@RequestMapping("/inno72/dictionary")
public class Inno72DictionaryController {
	@Resource
	private Inno72DictionaryService inno72DictionaryService;

	@RequestMapping(value = "/getBaseDict", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result getBaseDict(String type) {
		return inno72DictionaryService.getBaseDict(type);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<Inno72Dictionary> list = inno72DictionaryService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}
}
