package com.inno72.app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.app.mapper.Inno72SupplyChannelMapper;
import com.inno72.app.model.Inno72SupplyChannel;
import com.inno72.app.service.SupplyChannelService;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;

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
	public Result<String> initChannel(String machineId, String machineCode, List<Inno72SupplyChannel> channels) {
		if (channels == null) {
			return Results.failure("货道信息错误");
		}
		Condition condition = new Condition(Inno72SupplyChannel.class);
		String batchId = machineCode.substring(0, 2);
		condition.createCriteria().andEqualTo("machineId", machineId);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
		if (supplyChannelList == null || supplyChannelList.isEmpty()) {
			for (Inno72SupplyChannel channel : channels) {
				channel.setMachineId(machineId);
				channel.setId(StringUtil.getUUID());
				channel.setName("货道" + channel.getCode());
				channel.setGoodsCount(0);
				channel.setIsDelete(0);
				channel.setCreateId("系统");
				channel.setCreateTime(LocalDateTime.now());
				channel.setUpdateTime(LocalDateTime.now());
				channel.setUpdateId("系统");
				if ("18".equals(batchId)) {
					if (Integer.parseInt(channel.getCode()) < 20) {
						channel.setVolumeCount(11);
					} else if (Integer.parseInt(channel.getCode()) < 30 && Integer.parseInt(channel.getCode()) > 20) {
						channel.setVolumeCount(11);
					} else {
						channel.setVolumeCount(50);
					}
				} else if ("19".equals(batchId)) {
					if (Integer.parseInt(channel.getCode()) < 20) {
						channel.setVolumeCount(11);
					} else if (Integer.parseInt(channel.getCode()) < 40 && Integer.parseInt(channel.getCode()) > 20) {
						channel.setVolumeCount(5);
					} else {
						channel.setVolumeCount(50);
					}
				}
				super.save(channel);
			}
		}
		return Results.success();
	}

	@Override
	public Result<String> updateChannel(String machineId, String machineCode, List<Inno72SupplyChannel> channels) {
		if (channels == null || channels.isEmpty()) {
			return Results.failure("货道信息错误");
		}
		Condition condition = new Condition(Inno72SupplyChannel.class);
		String batchId = machineCode.substring(0, 2);
		condition.createCriteria().andEqualTo("machineId", machineId);
		List<Inno72SupplyChannel> supplyChannelList = inno72SupplyChannelMapper.selectByCondition(condition);
		if (supplyChannelList == null || supplyChannelList.isEmpty()) {
			initChannel(machineId, machineCode, channels);
		} else {
			List<String> delete = new ArrayList<>();
			List<Inno72SupplyChannel> newChannel = new ArrayList<>();
			for (Inno72SupplyChannel sc : channels) {

			}
		}
		return Results.success();
	}

}
