package com.inno72.store.service.impl;

import com.inno72.store.mapper.Inno72StoreOrderMapper;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.service.StoreOrderService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreOrderServiceImpl extends AbstractService<Inno72StoreOrder> implements StoreOrderService {
    @Resource
    private Inno72StoreOrderMapper inno72StoreOrderMapper;

}
