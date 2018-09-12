package com.inno72.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
public interface MachineService extends Service<Inno72Machine> {

    Inno72Machine findByCode(String machineCode);


    List<Inno72Machine> findAlarmAllMachine();
}
