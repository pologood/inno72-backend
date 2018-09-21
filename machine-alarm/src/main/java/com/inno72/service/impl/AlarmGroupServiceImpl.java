package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72AlarmGroupMapper;
import com.inno72.model.Inno72AlarmGroup;
import com.inno72.service.AlarmGroupService;

@Service
@Transactional
public class AlarmGroupServiceImpl extends AbstractService<Inno72AlarmGroup> implements AlarmGroupService  {

	@Resource
	private Inno72AlarmGroupMapper inno72AlarmGroupMapper;
	@Override
	public List<Inno72AlarmGroup> findByPage(Object condition) {
		return null;
	}

	@Override
	public Inno72AlarmGroup selectByParam(String areaCode) {
		Map<String,Object> map = new HashMap<>();
		map.put("areaCode",areaCode);
		Inno72AlarmGroup group = inno72AlarmGroupMapper.selectByParam(map);
		return group;
	}
}
