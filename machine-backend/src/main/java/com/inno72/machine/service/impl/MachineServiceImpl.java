package com.inno72.machine.service.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {
	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Override
	public Result<String> initMachine(String deviceId) {
		Inno72Machine initMachine = findBy("deviceId", deviceId);
		if (initMachine != null) {
			return Results.success(initMachine.getMachineCode());
		}
		String machineCode = StringUtil.getMachineCode();
		LocalDateTime now = LocalDateTime.now();
		Inno72Machine machine = new Inno72Machine();
		machine.setDeviceId(deviceId);
		machine.setId(StringUtil.getUUID());
		machine.setMachineCode(machineCode);
		machine.setMachineStatus(Inno72Machine.Machine_Status.INFACTORY.v());
		machine.setUpdateId("machine-backend");
		machine.setCreateId("machine-backend");
		machine.setCreateTime(now);
		machine.setUpdateTime(now);
		machine.setNetStatus(1);
		int result = inno72MachineMapper.insert(machine);
		if (result == 1) {
			return Results.success(machineCode);
		}
		return Results.failure("生成machineCode失败");
	}

	@Override
	public Result<String> updateNetStatus(String machineCode, Integer netStatus) {
		Inno72Machine machine = findBy("machineCode", machineCode);
		if (machine != null) {
			if (machine.getNetStatus() != netStatus) {
				machine.setNetStatus(netStatus);
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}
		} else {
			return Results.failure("机器code传入错误");
		}
		return Results.success();
	}

}
