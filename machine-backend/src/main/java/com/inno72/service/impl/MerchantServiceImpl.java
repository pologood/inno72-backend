package com.inno72.service.impl;

import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.model.Inno72Merchant;
import com.inno72.service.MerchantService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MerchantServiceImpl extends AbstractService<Inno72Merchant> implements MerchantService {
    @Resource
    private Inno72MerchantMapper inno72MerchantMapper;

}
