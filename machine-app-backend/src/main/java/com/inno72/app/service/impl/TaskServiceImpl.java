package com.inno72.app.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.app.mapper.Inno72TaskMachineMapper;
import com.inno72.app.model.Inno72TaskMachine;
import com.inno72.app.service.TaskService;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
@Service
@Transactional
public class TaskServiceImpl extends AbstractService<Inno72TaskMachine> implements TaskService {
	@Resource
	private Inno72TaskMachineMapper inno72TaskMachineMapper;

	@Override
	public Result<String> updateTaskStatus(Map<String, Object> msg) {

		String taskId = (String) Optional.of(msg).map(a -> a.get("taskId")).orElse("");
		String taskMsg = (String) Optional.of(msg).map(a -> a.get("taskMsg")).orElse("");
		Integer taskStatus = new Integer(Optional.of(msg).map(a -> a.get("taskStatus")).orElse("0").toString());

		if (StringUtil.isEmpty(taskId)) {
			return Results.failure("任务id为空");
		}
		if (taskStatus == 0) {
			return Results.failure("执行状态为空");
		}
		Inno72TaskMachine task = inno72TaskMachineMapper.selectByPrimaryKey(taskId);
		if (task == null) {
			return Results.failure("任务id错误");
		}
		task.setDoMsg(taskMsg);
		task.setDoStatus(taskStatus);
		task.setDoTime(LocalDateTime.now());
		inno72TaskMachineMapper.updateByPrimaryKey(task);
		return Results.success();
	}

}
