package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.service.InteractGoodsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractGoodsServiceImpl extends AbstractService<Inno72InteractGoods> implements InteractGoodsService {
    @Resource
    private Inno72InteractGoodsMapper inno72InteractGoodsMapper;

}
