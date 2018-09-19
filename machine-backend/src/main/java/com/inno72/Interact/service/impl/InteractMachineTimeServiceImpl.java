package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractMachineTimeMapper;
import com.inno72.Interact.model.Inno72InteractMachineTime;
import com.inno72.Interact.service.InteractMachineTimeService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMachineTimeServiceImpl extends AbstractService<Inno72InteractMachineTime> implements InteractMachineTimeService {
    @Resource
    private Inno72InteractMachineTimeMapper inno72InteractMachineTimeMapper;

}
