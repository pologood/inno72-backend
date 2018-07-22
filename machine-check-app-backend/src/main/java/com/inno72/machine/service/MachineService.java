package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Machine;

import java.util.List;

public interface MachineService extends Service<Inno72Machine> {

    public Result<String> setMachine(String machineId,String localeId);

    Result<List<Inno72Machine>> getMachineList();
}
