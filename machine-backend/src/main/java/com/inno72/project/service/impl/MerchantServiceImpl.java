package com.inno72.project.service.impl;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72MerchantMapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.service.MerchantService;
import com.inno72.project.vo.Inno72MerchantVo;

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
public class MerchantServiceImpl extends AbstractService<Inno72Merchant> implements MerchantService {
	private static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
	
    @Resource
    private Inno72MerchantMapper inno72MerchantMapper;
    
    @Override
	public void save(Inno72Merchant model) {
		logger.info("---------------------商户新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
    	
		super.save(model);
	}
    
    @Override
	public Result<String> delById(String id) {
		logger.info("--------------------商户删除-------------------");
		
		int n= inno72MerchantMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("店铺使用中，不能删除！");
		}
		Inno72Merchant model = inno72MerchantMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		model.setUpdateId("");
		
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public void update(Inno72Merchant model) {
		logger.info("---------------------商户更新-------------------");
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public List<Inno72MerchantVo> findByPage(String code,String keyword) {
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);
		
		return inno72MerchantMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Merchant> getList(Inno72Merchant model) {
		logger.info("---------------------获取商户列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition( Inno72Merchant.class);
	   	condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

}
