package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;
import com.inno72.model.MachineLogInfo;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
public interface MachineService extends Service<Inno72Machine> {

    /**
     * find machineInfo by machineStatus
     *
     * @param id
     * @return
     */
    Result<List<Inno72Machine>> findMachineByMachineStatus(int id);

    /**
     * find machineInfo by machineCode
     *
     * @param machineCode
     * @return
     */
    Result<Inno72Machine> findMachineByMachineCode(String machineCode);

    /**
     * find machineList
     *
     * @param
     * @return
     */
    Result<List<MachineLogInfo>> findMachineNetStatusOpenList(List<MachineLogInfo> machineLogInfos);

    /**
     * find machineList
     *
     * @param
     * @return
     */
    Result<List<MachineLogInfo>> findMachineNetStatusCloseList(List<MachineLogInfo> machineLogInfos);


    Inno72Machine findByCode(String machineCode);


    List<Inno72Machine> findAlarmAllMachine();
}
