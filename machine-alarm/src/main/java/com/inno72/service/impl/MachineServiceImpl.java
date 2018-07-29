package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Machine;
import com.inno72.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Override
    public Result<List<Inno72Machine>> findMachineByMachineStatus(int machineStatus) {

        List<Inno72Machine> list = inno72MachineMapper.findMachineByMachineStatus(machineStatus);

        return Results.success(list);
    }

    @Override
    public Result<Inno72Machine> findMachineByMachineCode(String machineCode) {
        Inno72Machine inno72Machine = inno72MachineMapper.findMachineByMachineCode(machineCode);
        return Results.success(inno72Machine);
    }


    @Override
    public List<Inno72Machine> findByPage(Object condition) {
        return null;
    }
}
