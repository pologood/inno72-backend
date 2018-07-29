package com.inno72.app.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.app.mapper.Inno72MachineMapper;
import com.inno72.app.model.Inno72Machine;
import com.inno72.app.model.Inno72SupplyChannel;
import com.inno72.app.service.MachineService;
import com.inno72.app.service.SupplyChannelService;
import com.inno72.common.AbstractService;
import com.inno72.common.MachineAppBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.plugin.http.HttpClient;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Autowired
	private SupplyChannelService supplyChannelService;

	@Resource
	private MachineAppBackendProperties machineAppBackendProperties;

	@Override
	public Result<String> generateMachineId(String deviceId) {
		Inno72Machine initMachine = findBy("deviceId", deviceId);
		if (initMachine != null) {
			return Results.success(initMachine.getMachineCode());
		}
		String machineCode = StringUtil.getMachineCode();
		Inno72Machine m = findBy("machineCode", machineCode);
		if (m != null) {
			machineCode = StringUtil.getMachineCode();
		}
		LocalDateTime now = LocalDateTime.now();
		Inno72Machine machine = new Inno72Machine();
		String machineId = StringUtil.getUUID();
		machine.setDeviceId(deviceId);
		machine.setId(machineId);
		machine.setMachineCode(machineCode);
		machine.setMachineStatus(Inno72Machine.Machine_Status.START.v());
		machine.setUpdateId("generateMachineId");
		machine.setCreateId("generateMachineId");
		machine.setCreateTime(now);
		machine.setUpdateTime(now);
		machine.setNetStatus(0);
		int result = inno72MachineMapper.insert(machine);
		if (result != 1) {
			return Results.failure("生成machineCode失败");
		}
		return Results.success(machineCode);
	}

	@Override
	public Result<String> initMachine(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		String bluetoothAddress = (String) msg.get("bluetoothAddress");
		Inno72Machine machine = findBy("machineCode", machineCode);
		if (machine == null) {
			return Results.failure("machineCode传入错误");
		}
		List<Inno72SupplyChannel> channels = JSON.parseArray(JSON.toJSONString(msg.get("channelJson")),
				Inno72SupplyChannel.class);
		if (channels == null || channels.isEmpty()) {
			return Results.failure("货道信息传入错误");
		}
		machine.setBluetoothAddress(bluetoothAddress);
		machine.setUpdateId("initMachine");
		machine.setUpdateTime(LocalDateTime.now());
		machine.setMachineStatus(Inno72Machine.Machine_Status.INIT.v());
		inno72MachineMapper.updateByPrimaryKeySelective(machine);
		Result<String> initResult = supplyChannelService.initChannel(machine.getId(), channels);
		if (initResult.getCode() != Result.SUCCESS) {
			return initResult;
		}
		return Results.success();
	}

	@Override
	public Result<String> updateMachineStatus(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		Integer status = new Integer(Optional.of(msg).map(a -> a.get("status")).orElse("0").toString());
		if (status == null || (status != Inno72Machine.Machine_Status.INLOCAL.v()
				&& status != Inno72Machine.Machine_Status.PASSTEST.v())) {
			return Results.failure("status传入错误");
		}
		Inno72Machine machine = findBy("machineCode", machineCode);
		if (machine == null) {
			return Results.failure("machineCode传入错误");

		}
		// 客户端会重复传入是3的状态值
		/*if (status == Inno72Machine.Machine_Status.PASSTEST.v()
				&& machine.getMachineStatus() != Inno72Machine.Machine_Status.PASSTEST.v()) {
			return Results.success();
		}
		if (status == Inno72Machine.Machine_Status.PASSTEST.v()
				&& machine.getMachineStatus() != Inno72Machine.Machine_Status.INIT.v()) {
			return Results.failure("status传入错误");
		}
		if (status == Inno72Machine.Machine_Status.INLOCAL.v()
				&& machine.getMachineStatus() != Inno72Machine.Machine_Status.PASSTEST.v()) {
			return Results.failure("status传入错误");
		}*/
		machine.setUpdateId("updateMachineStatus");
		machine.setUpdateTime(LocalDateTime.now());
		machine.setMachineStatus(status);
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result == 1) {
			return Results.success();
		}
		return Results.failure("更新失败");
	}

	@Override
	public Result<String> setMachineChannel(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		String channelCode = (String) Optional.of(msg).map(a -> a.get("channelCode")).orElse("");
		if (StringUtil.isEmpty(channelCode)) {
			return Results.failure("channelCode传入为空");
		}
		Integer operates = new Integer(Optional.of(msg).map(a -> a.get("operates")).orElse("0").toString());
		if (operates != 1 && operates != 2) {
			return Results.failure("operates传入错误");
		}
		logger.info("机器:{}货道:{}需要{}", machineCode, channelCode, operates == 1 ? "合并" : "拆分");
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		Map<String, Object> param = new HashMap<>();
		param.put("machineId", machines.get(0).getId());
		param.put("code", channelCode);
		String url = null;
		if (operates == 1) {
			url = machineAppBackendProperties.get("mergeChannelUrl");
		} else if (operates == 2) {
			url = machineAppBackendProperties.get("splitChannelUrl");
		} else {
			return Results.failure("操作类型传入错误");
		}
		String result = HttpClient.post(url, JSON.toJSONString(param));
		int code = JSON.parseObject(result).getInteger("code");
		if (code != 0) {
			return Results.failure(JSON.parseObject(result).getString("msg"));
		}
		return Results.success();
	}

	@Override
	public Result<Integer> getMachineStatus(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		return Results.success(machines.get(0).getMachineStatus());
	}

}
