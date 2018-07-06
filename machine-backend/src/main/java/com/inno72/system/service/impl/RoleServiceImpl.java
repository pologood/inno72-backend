package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.system.mapper.Inno72RoleMapper;
import com.inno72.system.model.Inno72Role;
import com.inno72.system.service.RoleService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Inno72Role> implements RoleService {
	@Resource
	private Inno72RoleMapper inno72RoleMapper;

}
