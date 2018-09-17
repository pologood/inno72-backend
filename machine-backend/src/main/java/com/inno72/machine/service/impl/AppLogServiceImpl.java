package com.inno72.machine.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.machine.mapper.Inno72AppLogMapper;
import com.inno72.machine.model.Inno72AppLog;
import com.inno72.machine.service.AppLogService;

/**
 * Created by CodeGenerator on 2018/09/17.
 */
@Service
@Transactional
public class AppLogServiceImpl extends AbstractService<Inno72AppLog> implements AppLogService {
	@Resource
	private Inno72AppLogMapper inno72AppLogMapper;

}
