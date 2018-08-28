package com.inno72.project.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72NoPlanInfoVo;

/**
 * Created by CodeGenerator on 2018/07/11.
 */
public interface ActivityPlanService extends Service<Inno72ActivityPlan> {

	Result<String> saveActPlan(Inno72ActivityPlanVo activityPlan);

	List<Inno72AdminAreaVo> selectAreaMachineList(String code, String level, String startTime, String endTime);

	List<Inno72ActivityPlanVo> selectPlanList(String code, String status, String type, String startTime,
			String endTime);

	Result<String> delById(String id, Integer status);

	Result<String> updateModel(Inno72ActivityPlanVo model);

	/**
	 * @describtion 查询没有活动的机器
	 * @author wxt
	 * @param taskTime
	 */
	List<Inno72NoPlanInfoVo> selectNoPlanMachineList(String taskTime);

	List<Map<String, Object>> selectPlanMachinDetailList(String planId);

	int selectPaiYangActivityCount();

}
