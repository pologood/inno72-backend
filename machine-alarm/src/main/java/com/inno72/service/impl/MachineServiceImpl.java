package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Machine;
import com.inno72.service.MachineService;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {


    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Override
    public List<Inno72Machine> findByPage(Object condition) {
        return null;
    }

    @Override
    public Inno72Machine findByCode(String machineCode) {
        return inno72MachineMapper.findMachineByMachineCode(machineCode);
    }

    @Override
    public List<Inno72Machine> findAlarmAllMachine() {
        return inno72MachineMapper.findAlarmAllMachine();
    }
}
