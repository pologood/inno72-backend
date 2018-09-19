package com.inno72.Interact.service.impl;

import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.service.InteractMachineService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMachineServiceImpl extends AbstractService<Inno72InteractMachine> implements InteractMachineService {
    @Resource
    private Inno72InteractMachineMapper inno72InteractMachineMapper;

}
