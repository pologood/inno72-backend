package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

    Result<List<Inno72Machine>> findMachineByMachineStatus(int id);

    Result<Inno72Machine> findMachineByMachineCode(String machineCode);


}
