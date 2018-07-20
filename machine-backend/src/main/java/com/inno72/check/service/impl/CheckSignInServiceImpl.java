package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {
    @Resource
    private Inno72CheckSignInMapper inno72CheckSignInMapper;

}
