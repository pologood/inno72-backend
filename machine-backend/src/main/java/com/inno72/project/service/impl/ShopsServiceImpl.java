package com.inno72.project.service.impl;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.ShopsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	}
	
	@Override
	public Result<String> delById(String id) {
		// TODO 店铺删除
		logger.info("--------------------店铺删除-------------------");
		int n= inno72ShopsMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("店铺使用中，不能删除！");
		}
		Inno72Shops model = inno72ShopsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		model.setUpdateId("");
		
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public void update(Inno72Shops model) {
		// TODO 店铺更新
		logger.info("--------------------店铺新增-------------------");
		model.setUpdateId("");
		super.update(model);
	}
    
	@Override
	public List<Inno72Shops> findByPage(String code,String keyword) {
		// TODO 分页列表查询
		logger.info("---------------------店铺分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		params.put("keyword", keyword);
		return inno72ShopsMapper.selectByPage(params);
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
