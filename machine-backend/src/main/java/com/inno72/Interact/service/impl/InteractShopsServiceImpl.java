package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractShopsMapper;
import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.service.InteractShopsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractShopsServiceImpl extends AbstractService<Inno72InteractShops> implements InteractShopsService {
    @Resource
    private Inno72InteractShopsMapper inno72InteractShopsMapper;

}
