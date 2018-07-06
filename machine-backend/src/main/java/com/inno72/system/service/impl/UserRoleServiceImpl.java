package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.system.mapper.Inno72UserRoleMapper;
import com.inno72.system.model.Inno72UserRole;
import com.inno72.system.service.UserRoleService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<Inno72UserRole> implements UserRoleService {
	@Resource
	private Inno72UserRoleMapper inno72UserRoleMapper;

}
