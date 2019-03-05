package com.inno72.project.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.project.mapper.Inno72ActivityInfoDescMapper;
import com.inno72.project.model.Inno72ActivityInfoDesc;
import com.inno72.project.service.Inno72ActivityInfoDescService;


/**
 * Created by CodeGenerator on 2019/01/11.
 */
@Service
@Transactional
public class Inno72ActivityInfoDescServiceImpl extends AbstractService<Inno72ActivityInfoDesc> implements Inno72ActivityInfoDescService {
    @Resource
    private Inno72ActivityInfoDescMapper inno72ActivityInfoDescMapper;

}
