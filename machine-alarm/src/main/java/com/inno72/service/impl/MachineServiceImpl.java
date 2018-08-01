package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Machine;
import com.inno72.model.MachineLogInfo;
import com.inno72.service.MachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public Result<List<MachineLogInfo>> findMachineNetStatusOpenList(List<MachineLogInfo> machineLogInfos) {
        List<String> machineList = new ArrayList<>();
        for (MachineLogInfo machineLogInfo : machineLogInfos) {
            machineList.add(machineLogInfo.getMachineId());
        }
        List<MachineLogInfo> machineLogInfos1 = new ArrayList<>();
        List<Inno72Machine> inno72Machines = inno72MachineMapper.findMachineNetStatusOpenList(machineList);
        for (Inno72Machine inno72Machine : inno72Machines) {
            if ((inno72Machine.getNetStatus()).equals(1)) {
                MachineLogInfo machineLogInfo = new MachineLogInfo();
                machineLogInfo.setMachineId(inno72Machine.getMachineCode());
                machineLogInfos1.add(machineLogInfo);
            }
        }

        return Results.success(machineLogInfos1);
    }


    @Override
    public Result<List<MachineLogInfo>> findMachineNetStatusCloseList(List<MachineLogInfo> machineLogInfos) {
        List<String> machineList = new ArrayList<>();
        for (MachineLogInfo machineLogInfo : machineLogInfos) {
            machineList.add(machineLogInfo.getMachineId());
        }
        List<MachineLogInfo> machineLogInfos1 = new ArrayList<>();
        List<Inno72Machine> inno72Machines = inno72MachineMapper.findMachineNetStatusOpenList(machineList);
        for (Inno72Machine inno72Machine : inno72Machines) {
            if ((inno72Machine.getNetStatus()).equals(0)) {
                MachineLogInfo machineLogInfo = new MachineLogInfo();
                machineLogInfo.setMachineId(inno72Machine.getMachineCode());
                machineLogInfos1.add(machineLogInfo);
            }
        }

        return Results.success(machineLogInfos1);
    }
}
