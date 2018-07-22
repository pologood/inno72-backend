package com.inno72.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.app.mapper.Inno72AppMapper;
import com.inno72.app.model.Inno72App;
import com.inno72.app.service.AppService;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
@Service
@Transactional
public class AppServiceImpl extends AbstractService<Inno72App> implements AppService {
	@Resource
	private Inno72AppMapper inno72AppMapper;

	@Override
	public Result<List<Inno72App>> getAppByBlong(String belong) {
		Condition condition = new Condition(Inno72App.class);
		condition.createCriteria().andEqualTo("appBelong", belong);
		List<Inno72App> app = inno72AppMapper.selectByCondition(condition);
		if (app == null || app.isEmpty()) {
			return Results.failure("belong传入错误");
		}
		return Results.success(app);
	}

}
