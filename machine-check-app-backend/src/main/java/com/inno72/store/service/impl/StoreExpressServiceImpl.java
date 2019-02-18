package com.inno72.store.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.store.mapper.Inno72StoreExpressMapper;
import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.service.StoreExpressService;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreExpressServiceImpl extends AbstractService<Inno72StoreExpress> implements StoreExpressService {
    @Resource
    private Inno72StoreExpressMapper inno72StoreExpressMapper;

}
