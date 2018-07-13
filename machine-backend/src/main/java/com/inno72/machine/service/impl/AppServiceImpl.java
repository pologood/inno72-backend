package com.inno72.machine.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.machine.mapper.Inno72AppMapper;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.service.AppService;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
@Service
@Transactional
public class AppServiceImpl extends AbstractService<Inno72App> implements AppService {
	@Resource
	private Inno72AppMapper inno72AppMapper;

}
