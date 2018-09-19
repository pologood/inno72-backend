package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.service.InteractMachineGoodsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMachineGoodsServiceImpl extends AbstractService<Inno72InteractMachineGoods> implements InteractMachineGoodsService {
    @Resource
    private Inno72InteractMachineGoodsMapper inno72InteractMachineGoodsMapper;

}
