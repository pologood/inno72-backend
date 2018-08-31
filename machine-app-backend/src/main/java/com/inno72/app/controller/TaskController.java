package com.inno72.app.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.app.service.TaskService;
import com.inno72.common.Result;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskController {
	@Resource
	private TaskService taskService;

	/**
	 * 更新任务状态
	 * 
	 * @param msg
	 * @return
	 */
	@RequestMapping(value = "/updateTaskStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> updateTaskStatus(@RequestBody Map<String, Object> msg) {
		return taskService.updateTaskStatus(msg);
	}

}