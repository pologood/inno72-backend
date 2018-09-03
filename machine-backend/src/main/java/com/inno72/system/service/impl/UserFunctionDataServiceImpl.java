package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.system.mapper.Inno72UserFunctionDataMapper;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.service.UserFunctionDataService;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@Service
@Transactional
public class UserFunctionDataServiceImpl extends AbstractService<Inno72UserFunctionData>
		implements UserFunctionDataService {
	@Resource
	private Inno72UserFunctionDataMapper inno72UserFunctionDataMapper;

}
