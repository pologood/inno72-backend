package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StoreFunctionMapper;
import com.inno72.store.model.Inno72StoreFunction;
import com.inno72.store.service.StoreFunctionService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreFunctionServiceImpl extends AbstractService<Inno72StoreFunction> implements StoreFunctionService {
    @Resource
    private Inno72StoreFunctionMapper inno72StoreFunctionMapper;

}
