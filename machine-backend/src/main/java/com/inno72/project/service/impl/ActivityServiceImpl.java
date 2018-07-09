package com.inno72.project.service.impl;


import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityMapper;
import com.inno72.project.model.Inno72Activity;
import com.inno72.project.service.ActivityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ActivityServiceImpl extends AbstractService<Inno72Activity> implements ActivityService {
	private static Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
	
    @Resource
    private Inno72ActivityMapper inno72ActivityMapper;

	@Override
	public void save(Inno72Activity model) {
		// TODO 活动添加
		logger.info("--------------------活动新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		super.save(model);
	}

	@Override
	public Result<String> delById(String id) {
		// TODO 活动逻辑删除
		logger.info("--------------------活动删除-------------------");
		int n= inno72ActivityMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("活动使用中，不能删除！");
		}
		Inno72Activity model = inno72ActivityMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		model.setUpdateId("");
		
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public void update(Inno72Activity model) {
		// TODO 活动更新
		logger.info("--------------------活动更新-------------------");
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public List<Inno72Activity> findByPage(String code,String keyword) {
		// TODO 分页列表查询
		logger.info("---------------------活动分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);
		
		return inno72ActivityMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72Activity> getList(Inno72Activity model) {
		// TODO 获取活动列表
		logger.info("---------------------获取活动列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition( Inno72Activity.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}
    
    

}
