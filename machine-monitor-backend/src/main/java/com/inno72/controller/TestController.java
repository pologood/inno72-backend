package com.inno72.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;

@RestController
@RequestMapping("/test")
public class TestController {
	@RequestMapping(value = "/test", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> test() {
		return ResultGenerator.genSuccessResult();
	}
}
