package com.inno72.app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.inno72.app.mapper.Inno72MachineBatchMapper;
import com.inno72.app.mapper.Inno72MachineMapper;
import com.inno72.app.model.Inno72Machine;
import com.inno72.app.model.Inno72MachineBatch;
import com.inno72.app.model.Inno72SupplyChannel;
import com.inno72.app.service.MachineService;
import com.inno72.app.service.SupplyChannelService;
import com.inno72.common.AbstractService;
import com.inno72.common.MachineAppBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.util.MapUtil;

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

	@Autowired
	private Inno72MachineBatchMapper inno72MachineBatchMapper;

	@Resource
	private MachineAppBackendProperties machineAppBackendProperties;

	@Override
	public Result<String> generateMachineId(String deviceId, String batcId) {
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("deviceId", deviceId).andNotEqualTo("machineStatus", -1);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		Inno72Machine initMachine = null;
		if (machines != null && machines.size() > 0) {
			initMachine = machines.get(0);
		}
		// Inno72Machine initMachine = findBy("deviceId", deviceId);
		if (initMachine != null) {
			String code = initMachine.getMachineCode();
			String pc = code.substring(0, 2);
			if (!pc.equals(batcId) && initMachine.getMachineStatus() == 1) {
				String machineCode = StringUtil.getMachineCode(batcId);
				initMachine.setMachineCode(machineCode);
				inno72MachineMapper.updateByPrimaryKeySelective(initMachine);
			}
			return Results.success(initMachine.getMachineCode());
		}
		String machineCode = StringUtil.getMachineCode(batcId);
		Inno72Machine m = findBy("machineCode", machineCode);
		if (m != null) {
			machineCode = StringUtil.getMachineCode(batcId);
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
		int result = inno72MachineMapper.insertSelective(machine);
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
		Result<String> initResult = supplyChannelService.initChannel(machine.getId(), machineCode, channels);
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
		if (status == Inno72Machine.Machine_Status.PASSTEST.v()
				&& machine.getMachineStatus() == Inno72Machine.Machine_Status.PASSTEST.v()) {
			return Results.success();
		}
		if (status == Inno72Machine.Machine_Status.PASSTEST.v()
				&& machine.getMachineStatus() != Inno72Machine.Machine_Status.INIT.v()) {
			return Results.failure("status传入错误");
		}
		if (status == Inno72Machine.Machine_Status.INLOCAL.v()
				&& machine.getMachineStatus() != Inno72Machine.Machine_Status.PASSTEST.v()) {
			return Results.failure("status传入错误");
		}

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

	@Override
	public Result<String> getMachineLocale(Map<String, Object> msg) {
		Integer type = MapUtil.getParam(msg, "type", Integer.class);
		String machineCode = MapUtil.getParam(msg, "machineCode", String.class);

		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		if (type == null || type == 0) {
			// 不需要验证机器状态
			String locale = inno72MachineMapper.selectMachineLocale(machineCode);
			return Results.success(locale);
		} else {
			Condition condition = new Condition(Inno72Machine.class);
			condition.createCriteria().andEqualTo("machineCode", machineCode);
			List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
			if (machines == null || machines.size() != 1) {
				return Results.failure("machineCode传入错误");
			}
			Inno72Machine machine = machines.get(0);
			if (machine.getMachineStatus() != 4 && machine.getMachineStatus() != 9) {
				return Results.failure("机器状态异常");
			}
			String locale = inno72MachineMapper.selectMachineLocale(machineCode);
			return Results.success(locale);
		}
	}

	@Override
	public Result<List<Inno72MachineBatch>> getMachineBatchs() {
		List<Inno72MachineBatch> all = inno72MachineBatchMapper.selectAll();
		return Results.success(all);
	}

	@Override
	public Result<String> updateMachineCode(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		String oldMachineCode = (String) Optional.of(msg).map(a -> a.get("oldMachineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode) || StringUtil.isEmpty(oldMachineCode)) {
			return Results.failure("machineCode为空");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		Condition condition1 = new Condition(Inno72Machine.class);
		condition1.createCriteria().andEqualTo("machineCode", oldMachineCode);
		List<Inno72Machine> machines1 = inno72MachineMapper.selectByCondition(condition1);
		if (machines1 == null || machines1.size() != 1) {
			return Results.failure("oldMachineCode传入错误");
		}
		Inno72Machine m1 = machines.get(0);
		Inno72Machine m2 = machines1.get(0);
		if (!m1.getDeviceId().equals(m2.getDeviceId())) {
			return Results.failure("两个机器的deviceId不一致");
		}
		m2.setMachineStatus(-1);
		m2.setUpdateTime(LocalDateTime.now());
		int result = inno72MachineMapper.updateByPrimaryKeySelective(m2);
		if (result == 1) {
			return Results.success();
		}
		return Results.failure("更新失败");
	}

	@Override
	public Result<List<Map<String, Object>>> getMachineChannels(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode为空");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		Condition condition1 = new Condition(Inno72SupplyChannel.class);
		condition1.createCriteria().andEqualTo("machineId", machines.get(0).getId()).andEqualTo("isRemove", 0);
		condition1.setOrderByClause("code*1");
		List<Inno72SupplyChannel> supplys = supplyChannelService.findByCondition(condition1);
		if (supplys == null) {
			return Results.failure("货道信息为空");
		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (Inno72SupplyChannel channel : supplys) {
			Map<String, Object> map = new HashMap<>();
			map.put("Coil_id", channel.getCode());
			map.put("iSlot_status", channel.getWorkStatus());
			map.put("lock_status", channel.getIsDelete());
			map.put("channel_id", channel.getId());

			list.add(map);
		}
		return Results.success(list);
	}

	@Override
	public Result<String> updateMachineChannels(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		List<Inno72SupplyChannel> channels = JSON.parseArray(JSON.toJSONString(msg.get("channelJson")),
				Inno72SupplyChannel.class);
		if (channels == null || channels.isEmpty()) {
			return Results.failure("货道信息传入错误");
		}
		Inno72Machine machine = findBy("machineCode", machineCode);
		if (machine == null) {
			return Results.failure("machineCode传入错误");
		}
		Map<String, Object> param = new HashMap<>();
		param.put("machineCode", machineCode);
		List<String> list = new ArrayList<>();
		for (Inno72SupplyChannel channel : channels) {
			list.add(channel.getCode());
		}
		param.put("list", list);
		String url = machineAppBackendProperties.get("updateMachineChannelsUrl");
		logger.info("url:{}", url);
		String result = HttpClient.post(url, JSON.toJSONString(param));
		int code = JSON.parseObject(result).getInteger("code");
		if (code != 0) {
			return Results.failure(JSON.parseObject(result).getString("msg"));
		}
		return Results.success();
	}

	@Override
	public Result<String> resetTuMachine(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		String deviceId = (String) Optional.of(msg).map(a -> a.get("deviceId")).orElse("");
		if (StringUtil.isEmpty(deviceId)) {
			return Results.failure("deviceId传入为空");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		Inno72Machine machine = machines.get(0);
		machine.setDeviceId(deviceId);
		machine.setMachineType(2);
		machine.setUpdateTime(LocalDateTime.now());
		inno72MachineMapper.updateByPrimaryKeySelective(machine);
		return Results.success();
	}

	@Override
	public Result<String> updateWifiPwd(Map<String, Object> msg) {
		String machineCode = (String) Optional.of(msg).map(a -> a.get("machineCode")).orElse("");
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("machineCode传入为空");
		}
		String wifiPwd = (String) Optional.of(msg).map(a -> a.get("wifiPwd")).orElse("");
		if (StringUtil.isEmpty(wifiPwd)) {
			return Results.failure("wifiPwd传入为空");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.size() != 1) {
			return Results.failure("machineCode传入错误");
		}
		Inno72Machine machine = machines.get(0);
		machine.setUpdateTime(LocalDateTime.now());
		machine.setWifiPwd(wifiPwd);
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result == 1) {
			return Results.success();
		}
		return Results.failure("更新失败");
	}

	@Override
	public Result<String> updateChannelStatus(Map<String, Object> msg) {
		String channelId = MapUtil.getParam(msg, "channelId", String.class);
		if (StringUtil.isEmpty(channelId)) {
			return Results.failure("channelId传入为空");
		}
		Integer lockStatus = MapUtil.getParam(msg, "lockStatus", Integer.class);
		if (lockStatus == null || (lockStatus != 0 && lockStatus != 1)) {
			return Results.failure("lockStatus传入错误");
		}
		Inno72SupplyChannel channel = supplyChannelService.findById(channelId);
		if (channel == null) {
			return Results.failure("channelId传入错误");
		}
		channel.setIsDelete(lockStatus);
		channel.setUpdateTime(LocalDateTime.now());
		supplyChannelService.update(channel);
		return Results.success();
	}

	@Override
	public Result<String> getServerTime(Map<String, Object> msg) {
		return Results.success(String.valueOf(System.currentTimeMillis()));
	}

}
