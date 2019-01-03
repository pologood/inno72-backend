package com.inno72.store.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.store.mapper.Inno72StoreGoodsMapper;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.service.StoreGoodsService;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreGoodsServiceImpl extends AbstractService<Inno72StoreGoods> implements StoreGoodsService {
    @Resource
    private Inno72StoreGoodsMapper inno72StoreGoodsMapper;

}
