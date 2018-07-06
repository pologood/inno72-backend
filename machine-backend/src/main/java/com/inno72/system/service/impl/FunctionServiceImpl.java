package com.inno72.system.service.impl;

import java.util.ArrayList;
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
import com.inno72.system.vo.FunctionTreeVo;

import tk.mybatis.mapper.entity.Condition;

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

	@Override
	public Result<FunctionTreeVo> findAllTree() {
		FunctionTreeVo vo = new FunctionTreeVo();
		vo.setId("XX");
		vo.setTitle("机器管理系统");
		Condition condition = new Condition(Inno72Function.class);
		condition.createCriteria().andEqualTo("functionLevel", 1);
		List<Inno72Function> first = findByCondition(condition);
		List<FunctionTreeVo> firstVo = new ArrayList<>();

		return null;
	}

}
