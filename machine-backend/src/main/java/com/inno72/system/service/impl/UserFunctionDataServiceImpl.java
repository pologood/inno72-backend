package com.inno72.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.system.mapper.Inno72FunctionMapper;
import com.inno72.system.mapper.Inno72UserFunctionDataMapper;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.model.Inno72RoleFunction;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.service.UserFunctionDataService;
import com.inno72.system.vo.FunctionTreeResultVo;
import com.inno72.system.vo.FunctionTreeResultVo.FunctionTreeVo;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
@Service
@Transactional
public class UserFunctionDataServiceImpl extends AbstractService<Inno72UserFunctionData>
		implements UserFunctionDataService {
	@Resource
	private Inno72UserFunctionDataMapper inno72UserFunctionDataMapper;
	@Resource
	private Inno72FunctionMapper inno72FunctionMapper;

	@Override
	public Result<FunctionTreeResultVo> findAllTree(String userId) {
		FunctionTreeResultVo re = new FunctionTreeResultVo();
		if (!StringUtil.isEmpty(userId)) {
			Condition condition1 = new Condition(Inno72RoleFunction.class);
			condition1.createCriteria().andEqualTo("userId", userId);
			List<Inno72UserFunctionData> functionData = inno72UserFunctionDataMapper.selectByCondition(condition1);
			List<String> rr = new ArrayList<>();
			for (Inno72UserFunctionData f : functionData) {
				rr.add(f.getId());
			}
			re.setFunctions(rr);
		}
		FunctionTreeVo root = new FunctionTreeVo();
		root.setId("XX");
		root.setTitle("机器管理系统");
		Condition condition = new Condition(Inno72Function.class);
		condition.createCriteria().andEqualTo("functionLevel", 1);
		List<Inno72Function> first = inno72FunctionMapper.selectByCondition(condition);
		List<FunctionTreeVo> firstVoList = new ArrayList<>();
		for (Inno72Function funFirst : first) {
			FunctionTreeVo funFirstVo = new FunctionTreeVo();
			funFirstVo.setId(funFirst.getId());
			funFirstVo.setTitle(funFirst.getFunctionDepict());
			firstVoList.add(funFirstVo);
			condition = new Condition(Inno72Function.class);
			condition.createCriteria().andEqualTo("functionLevel", 2).andEqualTo("parentId", funFirst.getId());
			List<Inno72Function> second = inno72FunctionMapper.selectByCondition(condition);
			List<FunctionTreeVo> secondVoList = new ArrayList<>();
			for (Inno72Function funSecond : second) {
				FunctionTreeVo funSecondVo = new FunctionTreeVo();
				funSecondVo.setId(funSecond.getId());
				funSecondVo.setTitle(funSecond.getFunctionDepict());
				secondVoList.add(funSecondVo);

				condition = new Condition(Inno72Function.class);
				condition.createCriteria().andEqualTo("functionLevel", 3).andEqualTo("parentId", funSecond.getId());
				List<Inno72Function> third = inno72FunctionMapper.selectByCondition(condition);
				List<FunctionTreeVo> thirdVoList = new ArrayList<>();

				for (Inno72Function funThird : third) {
					FunctionTreeVo funThirdVo = new FunctionTreeVo();
					funThirdVo.setId(funThird.getId());
					funThirdVo.setTitle(funThird.getFunctionDepict());
					thirdVoList.add(funThirdVo);
				}
				funSecondVo.setChildren(thirdVoList);
			}
			funFirstVo.setChildren(secondVoList);
		}
		root.setChildren(firstVoList);
		re.setTree(root);
		return Results.success(re);
	}
}
