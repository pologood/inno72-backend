package com.inno72.service.impl;

import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.service.GoodsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GoodsServiceImpl extends AbstractService<Inno72Goods> implements GoodsService {
    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

}
