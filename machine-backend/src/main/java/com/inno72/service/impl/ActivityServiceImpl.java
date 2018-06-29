package com.inno72.service.impl;

import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.service.ActivityService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ActivityServiceImpl extends AbstractService<Inno72Activity> implements ActivityService {
    @Resource
    private Inno72ActivityMapper inno72ActivityMapper;

}
