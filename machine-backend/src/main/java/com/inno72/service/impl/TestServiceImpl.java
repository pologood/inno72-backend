package com.inno72.service.impl;

import com.inno72.mapper.Inno72TestMapper;
import com.inno72.model.Inno72Test;
import com.inno72.service.TestService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/28.
 */
@Service
@Transactional
public class TestServiceImpl extends AbstractService<Inno72Test> implements TestService {
    @Resource
    private Inno72TestMapper inno72TestMapper;

}
