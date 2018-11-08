package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72SupplyChannelMapper;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.service.SupplyChannelService;

@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
    @Resource
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;
    @Override
    public List<Inno72SupplyChannel> findByPage(Object condition) {
        return null;
    }

    @Override
    public void closeSupply(Inno72SupplyChannel supplyChannel) {
        inno72SupplyChannelMapper.closeSupply(supplyChannel);
    }

	@Override
	public List<Inno72SupplyChannel> selectNormalSupply(String machineId, String goodsId) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("machineId",machineId);
    	map.put("goodsId",goodsId);
    	List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectNormalSupply(map);
		return list;
	}

	@Override
	public List<Inno72SupplyChannel> selectByParam(String machineId, String[] channelArray) {
		Map<String,Object> map = new HashMap<>();
		map.put("machineId",machineId);
		map.put("channelArray",channelArray);
		List<Inno72SupplyChannel> list = inno72SupplyChannelMapper.selectByParam(map);
		return list;
	}
}
