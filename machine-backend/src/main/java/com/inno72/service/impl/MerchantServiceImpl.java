package com.inno72.service.impl;

import com.inno72.mapper.Inno72MerchantMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.model.Inno72Locale;
import com.inno72.model.Inno72Merchant;
import com.inno72.service.MerchantService;

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
public class MerchantServiceImpl extends AbstractService<Inno72Merchant> implements MerchantService {
	private static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
	
    @Resource
    private Inno72MerchantMapper inno72MerchantMapper;
    
    @Override
	public void save(Inno72Merchant model) {
    	// TODO 商户新增
		logger.info("---------------------商户新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
    	
		super.save(model);
	}

	@Override
	public void deleteById(String id) {
		// TODO 商户删除
		logger.info("---------------------商户删除-------------------");
		Inno72Merchant model = inno72MerchantMapper.selectByPrimaryKey(id);
		model.setStatus(0);
		model.setCreateId("");
		model.setUpdateId("");
		super.update(model);
	}

	@Override
	public void update(Inno72Merchant model) {
		// TODO 商户更新
		logger.info("---------------------商户更新-------------------");
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public List<Inno72Merchant> findByPage(Inno72Merchant model) {
		// TODO 商户分页列表查询
		logger.info("---------------------商户分页列表查询-------------------");
		Condition condition = new Condition( Inno72Merchant.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByPage(condition);
	}

	@Override
	public List<Inno72Merchant> getList(Inno72Merchant model) {
		// TODO 获取商户列表
		logger.info("---------------------获取商户列表-------------------");
		model.setStatus(1);
		Condition condition = new Condition( Inno72Merchant.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

}
