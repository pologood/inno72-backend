package com.inno72.service.impl;

import com.inno72.mapper.Inno72LocaleMapper;
import com.inno72.model.Inno72Locale;
import com.inno72.service.LocaleService;

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
public class LocaleServiceImpl extends AbstractService<Inno72Locale> implements LocaleService {
	private static Logger logger = LoggerFactory.getLogger(LocaleServiceImpl.class);
	
    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;

	@Override
	public void save(Inno72Locale model) {
		// TODO 点位新增
		logger.info("---------------------点位新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		
		super.save(model);
	}

	@Override
	public void deleteById(String id) {
		// TODO 点位逻辑删除
		logger.info("---------------------点位删除-------------------");
		Inno72Locale model = inno72LocaleMapper.selectByPrimaryKey(id);
		model.setState(1);
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public void update(Inno72Locale model) {
		// TODO 点位信息更新
		logger.info("---------------------点位更新-------------------");
		
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
	}

	@Override
	public List<Inno72Locale> findByPage(Inno72Locale locale) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		Condition condition = new Condition( Inno72Locale.class);
	   	condition.createCriteria().andEqualTo(locale);
		return super.findByPage(condition);
	}
	
	@Override
	public List<Inno72Locale> getList(Inno72Locale locale) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		locale.setState(0);
		Condition condition = new Condition( Inno72Locale.class);
	   	condition.createCriteria().andEqualTo(locale);
		return super.findByCondition(condition);
	}
    
    

}
