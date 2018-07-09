package com.inno72.machine.service.impl;


import tk.mybatis.mapper.entity.Condition;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72AdminAreaMapper;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.service.LocaleService;
import com.inno72.machine.vo.Inno72LocaleVo;
import com.inno72.model.Inno72AdminArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

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
	public Result<String> delById(String id) {
		// TODO 点位逻辑删除
		logger.info("---------------------点位删除-------------------");
		int n= inno72LocaleMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("机器使用中，不能删除！");
		}
		Inno72Locale model = inno72LocaleMapper.selectByPrimaryKey(id);
		//判断是否可以删除
		model.setIsDelete(1);
		model.setUpdateId("");
		
		super.update(model);
		return ResultGenerator.genSuccessResult();
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
		String areaCode = vo.getAreaCode();//一共9位 省前2位后补0  市4位后补0 县6位后补0 商圈直接取
		String province=areaCode.substring(0,2)+"0000000";
		String city=areaCode.substring(0,4)+"00000";
		String district=areaCode.substring(0,6)+"000";
		vo.setProvince(province);
		vo.setCity(city);
		vo.setDistrict(district);
		vo.setCircle(areaCode);
		
		return inno72LocaleMapper.selectById(id);
	}

	@Override
	public List<Inno72LocaleVo> findByPage(String code,String keyword) {
		// TODO 分页列表查询
		logger.info("---------------------分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(code)) {
			int num =getlikeCode(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);
		
		List<Inno72LocaleVo> list = inno72LocaleMapper.selectByPage(params);
		Inno72AdminArea area= new Inno72AdminArea();
		
		for (Inno72LocaleVo inno72LocaleVo : list) {
			StringBuffer areaNmae=new StringBuffer();
			area =inno72AdminAreaMapper.selectByCode(inno72LocaleVo.getAreaCode());
			areaNmae.append(area.getProvince());
			areaNmae.append(area.getCity());
			areaNmae.append(area.getDistrict());
			if (!area.getCircle().equals("其他")) {
				areaNmae.append(area.getCircle());
			}
			inno72LocaleVo.setAreaName(areaNmae.toString());
			int userNum= inno72LocaleMapper.selectIsUseing(inno72LocaleVo.getId());
			inno72LocaleVo.setUserNum(userNum);
		}
		return list;
	}
	
	@Override
	public List<Inno72Locale> getList(Inno72Locale locale) {
		// TODO 分页列表查询
		logger.info("---------------------列表查询-------------------");
		locale.setIsDelete(0);
		Condition condition = new Condition( Inno72Locale.class);
	   	condition.createCriteria().andEqualTo(locale);
		return super.findByCondition(condition);
	}
	
	
	public int getlikeCode(String s){
		for (int i = s.length()-1; i >=0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i+1;
			}
		}
		return 0;
	}
	
	
    
    

}
