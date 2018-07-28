package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72ActivityPlanMapper;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.service.ActivityPlanService;
import com.inno72.vo.Inno72NoPlanInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/11.
 */
@Service
@Transactional
public class ActivityPlanServiceImpl extends AbstractService<Inno72ActivityPlan> implements ActivityPlanService {
    private static Logger logger = LoggerFactory.getLogger(ActivityPlanServiceImpl.class);

    @Resource
    private Inno72ActivityPlanMapper inno72ActivityPlanMapper;


    @Override
    public List<Inno72NoPlanInfoVo> selectNoPlanMachineList(String taskTime) {

        List<Inno72NoPlanInfoVo> noPlanedMachineList = inno72ActivityPlanMapper.selectNoPlanedMachine(taskTime);

        return noPlanedMachineList;
    }


    @Override
    public List<Inno72ActivityPlan> findByPage(Object condition) {
        return null;
    }
}
