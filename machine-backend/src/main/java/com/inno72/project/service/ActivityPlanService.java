package com.inno72.project.service;
import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;


/**
 * Created by CodeGenerator on 2018/07/11.
 */
public interface ActivityPlanService extends Service<Inno72ActivityPlan> {

	Result<String> saveActPlan(Inno72ActivityPlanVo activityPlan);

	List<Inno72AdminAreaVo> selectAreaMachineList(String code,String level);

	List<Inno72ActivityPlanVo> selectPlanList(String code, String startTime, String endTime);


}
