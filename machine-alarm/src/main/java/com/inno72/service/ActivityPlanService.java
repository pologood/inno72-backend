package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.vo.Inno72NoPlanInfoVo;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/11.
 */
public interface ActivityPlanService extends Service<Inno72ActivityPlan> {

    /**
     * @param taskTime
     * @describtion 查询没有活动的机器
     * @author wxt
     */
    List<Inno72NoPlanInfoVo> selectNoPlanMachineList(String taskTime);


}
