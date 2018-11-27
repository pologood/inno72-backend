package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72AlarmParamMapper;
import com.inno72.model.Inno72AlarmParam;
import com.inno72.service.AlarmParamService;
@Service
@Transactional
public class AlarmParamServiceImpl extends AbstractService<Inno72AlarmParam> implements AlarmParamService {

	@Resource
	private Inno72AlarmParamMapper inno72AlarmParamMapper;

	@Override
	public List<Inno72AlarmParam> findByPage(Object condition) {
		return null;
	}

	@Override
	public Inno72AlarmParam findByAlarmType(Integer alarmType) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("alarmType",alarmType);
		Inno72AlarmParam alarmParam = inno72AlarmParamMapper.selectByParam(paramMap);
		return alarmParam;
	}
}
