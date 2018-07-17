package com.inno72.project.mapper;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72NoPlanInfoVo;

import java.util.List;
import java.util.Map;


@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {
	
	List<Inno72ActivityPlanVo> selectPlanList(Map<String, Object> params);
	
	List<Inno72AdminAreaVo> selectAreaMachineList(Map<String, Object> params);
	
	List<String> selectPlanedMachine(Map<String, Object> params);
	
	Inno72ActivityPlanVo selectPlanDetail(String id);

	int  selectPlanIsState(String id);

	List<Inno72NoPlanInfoVo> selectNoPlanedMachine(String taskTime);
}