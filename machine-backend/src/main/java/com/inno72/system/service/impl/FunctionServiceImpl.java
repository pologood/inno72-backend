package com.inno72.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
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

	@Override
	public Result<List<Inno72Function>> findFunctions(String keyword) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		List<Inno72Function> users = inno72FunctionMapper.selectFunctionsByPage(param);
		return Results.success(users);
	}

}
