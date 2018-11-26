package com.inno72.machine.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineLocaleDetailMapper;
import com.inno72.machine.model.Inno72MachineLocaleDetail;
import com.inno72.machine.service.MachineLocaleDetailService;

/**
 * Created by CodeGenerator on 2018/11/08.
 */
@Service
@Transactional
public class MachineLocaleDetailServiceImpl extends AbstractService<Inno72MachineLocaleDetail>
		implements MachineLocaleDetailService {
	@Resource
	private Inno72MachineLocaleDetailMapper inno72MachineLocaleDetailMapper;

	@Override
	public List<Map<String, Object>> findListByPage(String code, String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);

		List<Map<String, Object>> list = inno72MachineLocaleDetailMapper.selectMachineTimeByPage(params);
		return list;
	}

	@Override
	public List<Map<String, Object>> findMachineLocaleDetail(String machineId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("machineId", machineId);
		List<Map<String, Object>> list = inno72MachineLocaleDetailMapper.selectMachineLocaleDetail(params);
		return list;
	}

}
