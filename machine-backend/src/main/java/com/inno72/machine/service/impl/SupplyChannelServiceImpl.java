package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72SupplyChannelMapper;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.model.Inno72SupplyChannelHist;
import com.inno72.machine.service.SupplyChannelService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/10.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
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

}
