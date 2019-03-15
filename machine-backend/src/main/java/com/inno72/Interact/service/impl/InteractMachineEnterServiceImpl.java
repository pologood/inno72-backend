package com.inno72.Interact.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72MachineEnterMapper;
import com.inno72.Interact.model.Inno72MachineEnter;
import com.inno72.Interact.service.InteractMachineEnterService;
import com.inno72.Interact.vo.MachineEnterVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;

/**
 * Created by CodeGenerator on 2019/03/15.
 */
@Service
@Transactional
public class InteractMachineEnterServiceImpl extends AbstractService<Inno72MachineEnter>
		implements InteractMachineEnterService {
	@Resource
	private Inno72MachineEnterMapper inno72MachineEnterMapper;

	@Override
	public List<MachineEnterVo> findByPage(String interactId, Integer status, String machineCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("interactId", interactId);
		params.put("status", status);
		params.put("machineCode", machineCode);

		return inno72MachineEnterMapper.selectByPage(params);
	}

	@Override
	public Result<Object> updateBatchEnter(String interactId, String enterType) {

		return null;
	}

	@Override
	public Result<Object> updateEnter(String machineId, String enterType) {

		return null;
	}

}
