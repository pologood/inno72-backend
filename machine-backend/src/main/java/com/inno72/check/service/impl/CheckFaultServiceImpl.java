package com.inno72.check.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.AbstractService;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/19.
 */
@Service
@Transactional
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {
    @Resource
    private Inno72CheckFaultMapper inno72CheckFaultMapper;

}
