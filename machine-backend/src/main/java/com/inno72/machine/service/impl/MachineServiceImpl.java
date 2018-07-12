package com.inno72.machine.service.impl;

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
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.system.model.Inno72User;

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

	@Override
	public Result<String> initMachine(String deviceId, String channelJson) {
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
		if (result != 1) {
			return Results.failure("生成machineCode失败");
		}
		List<Inno72SupplyChannel> channels = JSON.parseArray(channelJson, Inno72SupplyChannel.class);
		Result<String> initResult = supplyChannelService.initChannel(machineCode, channels);
		if (initResult.getCode() != Result.SUCCESS) {
			return initResult;
		}
		return Results.success(machineCode);
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

	@Override
	public Result<List<Inno72Machine>> findMachines(String machineCode, String localCode) {
		Map<String, Object> param = new HashMap<>();
		if (StringUtil.isNotEmpty(localCode)) {
			int num = getlikeCode(localCode);
			if (num < 4) {
				num = 3;
			}
			String likeCode = localCode.substring(0, num);
			param.put("code", likeCode);
			param.put("num", num);
		}
		param.put("machineCode", machineCode);

		List<Inno72Machine> machines = inno72MachineMapper.selectMachinesByPage(param);
		return Results.success(machines);
	}

	public int getlikeCode(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i + 1;
			}
		}
		return 0;
	}

	@Override
	public Result<String> updateLocale(String id, String localeId, String address) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(id);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		machine.setLocaleId(localeId);
		machine.setAddress(address);
		machine.setUpdateId(mUser.getId());
		machine.setUpdateTime(LocalDateTime.now());
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result != 1) {
			return Results.failure("修改点位失败");
		}
		return Results.success();

	}

	@Override
	public Result<List<ChannelListVo>> channelList(String id) {
		Inno72Machine machine = findById(id);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		return supplyChannelService.findChannelListByMachineId(machine.getMachineCode());
	}

	@Override
	public Result<String> deleteChannel(String channelId, Integer status) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Result<String> result = supplyChannelService.deleteChannel(channelId, status);

		if (result.getCode() == Result.SUCCESS) {
			Inno72Machine machine = findBy("machineCode", result.getData());
			if (machine != null) {
				machine.setUpdateId(mUser.getId());
				machine.setUpdateTime(LocalDateTime.now());
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}

		}
		return result;

	}

	@Override
	public Result<String> updateGoodsCount(String channelId, Integer count) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Result<String> result = supplyChannelService.updateGoodsCount(channelId, count);
		if (result.getCode() == Result.SUCCESS) {
			Inno72Machine machine = findBy("machineCode", result.getData());
			if (machine != null) {
				machine.setUpdateId(mUser.getId());
				machine.setUpdateTime(LocalDateTime.now());
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}

		}
		return result;

	}

}
