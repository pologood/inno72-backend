package com.inno72.merchant.service.impl;

import com.inno72.merchant.mapper.Inno72SupplyChannelDictMapper;
import com.inno72.merchant.model.Inno72SupplyChannelDict;
import com.inno72.merchant.service.SupplyChannelDictService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelDictServiceImpl extends AbstractService<Inno72SupplyChannelDict> implements SupplyChannelDictService {
    @Resource
    private Inno72SupplyChannelDictMapper inno72SupplyChannelDictMapper;

    @Override
    public List<Inno72SupplyChannelDict> getAll() {
        return inno72SupplyChannelDictMapper.selectAll();
    }
}
