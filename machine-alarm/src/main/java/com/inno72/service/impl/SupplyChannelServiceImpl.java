package com.inno72.service.impl;

import java.util.List;

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
}
