package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityPlanMapper;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.service.ActivityPlanService;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/11.
 */
@Service
@Transactional
public class ActivityPlanServiceImpl extends AbstractService<Inno72ActivityPlan> implements ActivityPlanService {
    @Resource
    private Inno72ActivityPlanMapper inno72ActivityPlanMapper;

	@Override
	public Result<String> saveActPlan(Inno72ActivityPlan activityPlan) {
		
		//保存计划信息   时间处理
		
		
		
		
		//保存计划机器关系
		
		
		
		//保存计划游戏结果（先保存商品，优惠券）
		
		//保存商品
		
		//保存优惠券
		
		//保存计划游戏结果
		
		
		
		return null;
	}

	@Override
	public List<Inno72ActivityPlanVo> selectByPage(Object condition) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		
		return inno72ActivityPlanMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72AdminAreaVo> selectAreaMachineList(String code,String level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		
		if (StringUtil.isEmpty(code)) {
			params.put("level", 1);
	   	}
		if (level.equals("1")) {
			params.put("num", 2);
		}else if (level.equals("2")) {
			params.put("num", 6);
		}else if (level.equals("3")) {
			params.put("num", 9);
		}
	   	return inno72ActivityPlanMapper.selectAreaMachineList(params);
	}
	
	

}
