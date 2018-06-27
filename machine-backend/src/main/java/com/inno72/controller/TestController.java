package com.inno72.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.service.ITestService;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
	@Autowired
	private ITestService TestService;

	@GetMapping("/test")
	public Result<String> add(String s) {
		return TestService.test();
	}
}
