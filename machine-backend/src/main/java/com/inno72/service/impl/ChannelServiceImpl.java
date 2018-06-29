package com.inno72.service.impl;

import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.model.Inno72Channel;
import com.inno72.service.ChannelService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ChannelServiceImpl extends AbstractService<Inno72Channel> implements ChannelService {
    @Resource
    private Inno72ChannelMapper inno72ChannelMapper;

}
