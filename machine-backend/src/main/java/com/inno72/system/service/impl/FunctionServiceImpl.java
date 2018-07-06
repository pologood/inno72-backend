package com.inno72.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.system.mapper.Inno72FunctionMapper;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.service.FunctionService;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@Service
@Transactional
public class FunctionServiceImpl extends AbstractService<Inno72Function> implements FunctionService {
	@Resource
	private Inno72FunctionMapper inno72FunctionMapper;

	@Override
	public List<Inno72Function> findFunctionsByUserId(String id) {
		return inno72FunctionMapper.findFunctionsByUserId(id);
	}

}
