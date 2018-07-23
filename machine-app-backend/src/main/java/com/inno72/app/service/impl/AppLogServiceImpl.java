package com.inno72.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.app.mapper.Inno72AppLogMapper;
import com.inno72.app.model.Inno72AppLog;
import com.inno72.app.service.AppLogService;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;

/**
 * Created by CodeGenerator on 2018/07/22.
 */
@Service
@Transactional
public class AppLogServiceImpl extends AbstractService<Inno72AppLog> implements AppLogService {
	@Resource
	private Inno72AppLogMapper inno72AppLogMapper;

	@Override
	public Result<String> sendLogInfo(Map<String, Object> msg) {
		List<Inno72AppLog> logs = JSON.parseArray(JSON.toJSONString(msg.get("logs")), Inno72AppLog.class);
		if (logs == null || logs.isEmpty()) {
			return Results.failure("数据格式发送失败");
		}
		for (Inno72AppLog log : logs) {
			log.setId(StringUtil.getUUID());
			log.setStatus(1);
			log.setReciveTime(LocalDateTime.now());
			inno72AppLogMapper.insert(log);
		}
		return Results.success();
	}

}
