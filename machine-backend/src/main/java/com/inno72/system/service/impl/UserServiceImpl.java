package com.inno72.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.mapper.Inno72UserMapper;
import com.inno72.system.model.Inno72User;
import com.inno72.system.service.UserService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<Inno72User> implements UserService {
	@Resource
	private Inno72UserMapper inno72UserMapper;

	@Override
	public Result<Inno72User> getUserByUserId(String userId) {
		Condition condition = new Condition(Inno72User.class);
		condition.createCriteria().andEqualTo("userId", userId).andEqualTo("isDelete", 0);
		List<Inno72User> users = inno72UserMapper.selectByCondition(condition);
		if (users != null && users.size() != 0) {
			return ResultGenerator.genSuccessResult(users.get(0));
		}
		return ResultGenerator.genSuccessResult(null);

	}

}
