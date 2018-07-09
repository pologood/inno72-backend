package com.inno72.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.MachineAppStatus;
import com.mongodb.WriteResult;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private MachineMonitorBackendProperties machineMonitorBackendProperties;

	@Autowired
	private MongoOperations mongoTpl;

	@RequestMapping(value = "/test", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> test() {
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is("1864272070630"));
		WriteResult a = mongoTpl.remove(query, "MachineAppStatus");
		MachineAppStatus status = new MachineAppStatus();
		status.setMachineId("1864272070630");
		status.setStatus(null);
		mongoTpl.save(status, "MachineAppStatus");
		return ResultGenerator.genSuccessResult(JSON.toJSONString(a));
	}
}
