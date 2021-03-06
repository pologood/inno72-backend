package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StorekeeperStorteMapper;
import com.inno72.store.model.Inno72StorekeeperStorte;
import com.inno72.store.service.StorekeeperStorteService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StorekeeperStorteServiceImpl extends AbstractService<Inno72StorekeeperStorte> implements StorekeeperStorteService {
    @Resource
    private Inno72StorekeeperStorteMapper inno72StorekeeperStorteMapper;

}
