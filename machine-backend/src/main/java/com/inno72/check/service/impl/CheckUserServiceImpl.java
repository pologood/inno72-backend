package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
@Service
@Transactional
public class CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {
    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;

}
