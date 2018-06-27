package com.inno72.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.TestMapper;
import com.inno72.model.Test;
import com.inno72.service.ITestService;

@Service
public class TestServiceImpl implements ITestService {
	@Autowired
	private TestMapper testMapper;

	@Override
	public Result<String> test() {
		Test t = testMapper.test();
		return Results.success(Optional.ofNullable(t).map(Test::getMachineId).orElse("没找到"));
	}

}
