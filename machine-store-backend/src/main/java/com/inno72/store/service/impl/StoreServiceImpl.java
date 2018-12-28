package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StoreMapper;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.service.StoreService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreServiceImpl extends AbstractService<Inno72Store> implements StoreService {
    @Resource
    private Inno72StoreMapper inno72StoreMapper;

}
