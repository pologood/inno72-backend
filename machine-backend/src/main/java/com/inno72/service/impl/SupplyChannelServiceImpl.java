package com.inno72.service.impl;

import com.inno72.mapper.Inno72SupplyChannelMapper;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.service.SupplyChannelService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelServiceImpl extends AbstractService<Inno72SupplyChannel> implements SupplyChannelService {
    @Resource
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

}
