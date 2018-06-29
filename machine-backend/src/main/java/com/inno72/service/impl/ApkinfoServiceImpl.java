package com.inno72.service.impl;

import com.inno72.mapper.Inno72ApkinfoMapper;
import com.inno72.model.Inno72Apkinfo;
import com.inno72.service.ApkinfoService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ApkinfoServiceImpl extends AbstractService<Inno72Apkinfo> implements ApkinfoService {
    @Resource
    private Inno72ApkinfoMapper inno72ApkinfoMapper;

}
