package com.inno72.service.impl;

import com.inno72.mapper.Inno72SupplyChannelDictMapper;
import com.inno72.model.Inno72SupplyChannelDict;
import com.inno72.service.SupplyChannelDictService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelDictServiceImpl extends AbstractService<Inno72SupplyChannelDict> implements SupplyChannelDictService {
    @Resource
    private Inno72SupplyChannelDictMapper inno72SupplyChannelDictMapper;

}
