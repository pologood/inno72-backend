package com.inno72.service.impl;

import com.inno72.mapper.Inno72LocaleMapper;
import com.inno72.model.Inno72Locale;
import com.inno72.service.LocaleService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class LocaleServiceImpl extends AbstractService<Inno72Locale> implements LocaleService {
    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;

}
