package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72SupplyChannelMapper;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.model.Inno72SupplyChannelHist;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/10.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

	@Override
	public Result<String> initChannel(String machineCode, List<Inno72SupplyChannel> channels) {
		if (channels == null) {
			return Results.failure("货道信息错误");
		}
		Condition condition = new Condition(Inno72SupplyChannelHist.class);
		condition.createCriteria().andEqualTo("machineId", machineCode);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
		if (supplyChannelList != null) {
			for (Inno72SupplyChannel channel : channels) {
				channel.setMachineId(machineCode);
				channel.setId(StringUtil.getUUID());
				channel.setName("货道" + channel.getCode());
				channel.setGoodsCount(0);
				channel.setIsDelete(0);
				channel.setCreateId("系统");
				channel.setCreateTime(LocalDateTime.now());
				channel.setUpdateTime(LocalDateTime.now());
				channel.setUpdateId("系统");
				super.save(channel);
			}
		}
		return Results.success();
	}

	@Override
	public Result<List<ChannelListVo>> findChannelListByMachineId(String id) {
		return Results.success(inno72SupplyChannelMapper.selectChannelListByMachineId(id));
	}

	@Override
	public Result<String> deleteChannel(List<UpdateMachineChannelVo> channels) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		if (channels == null) {
			return Results.failure("货道信息传入错误");
		}
		String machineId = "";
		for (UpdateMachineChannelVo chan : channels) {
			Inno72SupplyChannel channel = findById(chan.getChannelId());
			if (channel != null) {
				channel.setIsDelete(chan.getStatus());
				channel.setUpdateTime(LocalDateTime.now());
				channel.setUpdateId(mUser.getId());
				inno72SupplyChannelMapper.updateByPrimaryKeySelective(channel);
				machineId = channel.getMachineId();
			}

		}
		return Results.success(machineId);
	}

	@Override
	public Result<String> updateGoodsCount(List<UpdateMachineChannelVo> channels) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		if (channels == null) {
			return Results.failure("货道信息传入错误");
		}
		String machineId = "";
		for (UpdateMachineChannelVo chan : channels) {
			Inno72SupplyChannel channel = findById(chan.getChannelId());
			if (channel != null) {
				channel.setGoodsCount(chan.getCount());
				channel.setUpdateTime(LocalDateTime.now());
				channel.setUpdateId(mUser.getId());
				inno72SupplyChannelMapper.updateByPrimaryKeySelective(channel);
				machineId = channel.getMachineId();
			}

		}
		return Results.success(machineId);
	}
}
