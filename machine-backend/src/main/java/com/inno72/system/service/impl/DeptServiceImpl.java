package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.mapper.Inno72DeptMapper;
import com.inno72.system.model.Inno72Dept;
import com.inno72.system.service.DeptService;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@Service
@Transactional
public class DeptServiceImpl extends AbstractService<Inno72Dept> implements DeptService {
	@Resource
	private Inno72DeptMapper inno72DeptMapper;

	@Override
	public Result<String> deleteAll() {
		inno72DeptMapper.deleteAll();
		return ResultGenerator.genSuccessResult();
	}

}
