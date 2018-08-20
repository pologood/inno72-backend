package com.inno72.service.impl;

import com.inno72.mapper.Inno72AppScreenShotMapper;
import com.inno72.model.Inno72AppScreenShot;
import com.inno72.service.AppScreenShotService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/08/17.
 */
@Service
@Transactional
public class AppScreenShotServiceImpl extends AbstractService<Inno72AppScreenShot> implements AppScreenShotService {
    @Resource
    private Inno72AppScreenShotMapper inno72AppScreenShotMapper;

}
