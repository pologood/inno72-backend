package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StorekeeperFunctionMapper;
import com.inno72.store.model.Inno72StorekeeperFunction;
import com.inno72.store.service.StorekeeperFunctionService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StorekeeperFunctionServiceImpl extends AbstractService<Inno72StorekeeperFunction> implements StorekeeperFunctionService {
    @Resource
    private Inno72StorekeeperFunctionMapper inno72StorekeeperFunctionMapper;

}
