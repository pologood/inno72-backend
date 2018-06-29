package com.inno72.service.impl;

import com.inno72.mapper.Inno72AdminAreaMapper;
import com.inno72.model.Inno72AdminArea;
import com.inno72.service.AdminAreaService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class AdminAreaServiceImpl extends AbstractService<Inno72AdminArea> implements AdminAreaService {
    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

}
