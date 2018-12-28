package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StorekeeperMapper;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StorekeeperService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StorekeeperServiceImpl extends AbstractService<Inno72Storekeeper> implements StorekeeperService {
    @Resource
    private Inno72StorekeeperMapper inno72StorekeeperMapper;

}
