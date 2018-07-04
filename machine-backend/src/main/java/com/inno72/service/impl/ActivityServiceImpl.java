package com.inno72.service.impl;

import com.inno72.mapper.Inno72ActivityMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.service.ActivityService;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public void deleteById(String id) {
		// TODO 活动逻辑删除
		logger.info("--------------------活动删除-------------------");
		Inno72Activity model = inno72ActivityMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		
		model.setCreateId("");
		model.setUpdateId("");
		
		super.deleteById(id);
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
	public List<Inno72Activity> findByPage(Inno72Activity model) {
		// TODO 分页列表查询
		logger.info("---------------------活动分页列表查询-------------------");
		Condition condition = new Condition( Inno72Activity.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByPage(condition);
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
