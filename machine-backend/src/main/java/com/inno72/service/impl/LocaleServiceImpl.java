package com.inno72.service.impl;

import com.inno72.mapper.Inno72LocaleMapper;
import com.inno72.model.Inno72Locale;
import com.inno72.service.LocaleService;
import com.inno72.vo.Inno72LocaleVo;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		model.setIsDelete(1);
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
	public Inno72LocaleVo findById(String id) {
		// TODO Auto-generated method stub
		
		Inno72LocaleVo vo = inno72LocaleMapper.selectById(id);
		String areCode = vo.getAreCode();//一共9位 省前2位后补0  市4位后补0 县6位后补0 商圈直接取
		String province=areCode.substring(0,1);
		String city=areCode.substring(0,3);
		String district=areCode.substring(0,5);
		vo.setProvince(province);
		vo.setCity(city);
		vo.setDistrict(district);
		
		return inno72LocaleMapper.selectById(id);
	}

	@Override
	public List<Inno72LocaleVo> findByPage(String code,String keyword) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		params.put("keyword", keyword);
		return inno72LocaleMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72Locale> getList(Inno72Locale locale) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		locale.setIsDelete(0);
		Condition condition = new Condition( Inno72Locale.class);
	   	condition.createCriteria().andEqualTo(locale);
		return super.findByCondition(condition);
	}
	
	
    
    

}
