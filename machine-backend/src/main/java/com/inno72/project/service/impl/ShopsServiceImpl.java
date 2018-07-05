package com.inno72.project.service.impl;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.ShopsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class ShopsServiceImpl extends AbstractService<Inno72Shops> implements ShopsService {
	private static Logger logger = LoggerFactory.getLogger(ShopsServiceImpl.class);
	
    @Resource
    private Inno72ShopsMapper inno72ShopsMapper;

	@Override
	public void save(Inno72Shops model) {
		// TODO 活动添加
		logger.info("--------------------活动新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		super.save(model);
		super.save(model);
	}

	@Override
	public void deleteById(String id) {
		// TODO 店铺删除
		logger.info("--------------------店铺删除-------------------");
		Inno72Shops model = inno72ShopsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);
		model.setUpdateId("");
		super.update(model);
	}

	@Override
	public void update(Inno72Shops model) {
		// TODO 店铺更新
		logger.info("--------------------店铺新增-------------------");
		model.setUpdateId("");
		super.update(model);
	}
    
	@Override
	public List<Inno72Shops> findByPage(Inno72Shops model) {
		// TODO 分页列表查询
		logger.info("---------------------店铺分页列表查询-------------------");
		Condition condition = new Condition( Inno72Shops.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByPage(condition);
	}
	
	@Override
	public List<Inno72Shops> getList(Inno72Shops model) {
		// TODO 获取店铺列表
		logger.info("---------------------获取店铺列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition( Inno72Shops.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

}
