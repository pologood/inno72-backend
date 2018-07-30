package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.vo.Inno72NoPlanInfoVo;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/11.
 * @author
 */
public interface ActivityPlanService extends Service<Inno72ActivityPlan> {

    /**
     * find no plan machine
     * @return
     * @param taskTime
     */
    List<Inno72NoPlanInfoVo> selectNoPlanMachineList(String taskTime);


}
