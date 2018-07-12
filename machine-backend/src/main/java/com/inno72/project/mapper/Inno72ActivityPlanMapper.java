package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;


@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {
	
	List<Inno72ActivityPlanVo> selectByPage(Map<String, Object> params);
	
	List<Inno72AdminAreaVo> selectAreaMachineList(Map<String, Object> params);
	
	
	
}