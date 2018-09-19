package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractMerchantMapper;
import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMerchantServiceImpl extends AbstractService<Inno72InteractMerchant> implements InteractMerchantService {
    @Resource
    private Inno72InteractMerchantMapper inno72InteractMerchantMapper;

}
