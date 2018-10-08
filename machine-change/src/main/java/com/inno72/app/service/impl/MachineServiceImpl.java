package com.inno72.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.app.mapper.Inno72MachineMapper;
import com.inno72.app.model.Inno72Machine;
import com.inno72.app.service.MachineService;
import com.inno72.common.AbstractService;

@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {
	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Override
	public Inno72Machine getMachineByCode(String machineCode) {
		return inno72MachineMapper.selectByCode(machineCode);
	}

	@Override
	public List<Inno72Machine> findByPage(Object condition) {
		return null;
	}
}
