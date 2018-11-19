package com.inno72.project.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.project.service.Inno72DictionaryService;

/**
 * Created by CodeGenerator on 2018/11/16.
 */
@RestController
@RequestMapping("/project/dictionary")
public class DictionaryController {
	@Resource
	private Inno72DictionaryService inno72DictionaryService;

	@RequestMapping(value = "/getBaseDict", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result getBaseDict(String type) {
		return inno72DictionaryService.getBaseDict(type);
	}
}
