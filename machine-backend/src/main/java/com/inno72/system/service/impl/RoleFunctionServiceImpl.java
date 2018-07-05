package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.system.mapper.Inno72RoleFunctionMapper;
import com.inno72.system.model.Inno72RoleFunction;
import com.inno72.system.service.RoleFunctionService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@Service
@Transactional
public class RoleFunctionServiceImpl extends AbstractService<Inno72RoleFunction> implements RoleFunctionService {
	@Resource
	private Inno72RoleFunctionMapper inno72RoleFunctionMapper;

}
