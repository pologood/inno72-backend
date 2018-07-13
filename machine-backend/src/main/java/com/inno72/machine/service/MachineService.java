package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Machine;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

	Result<String> initMachine(String deviceId, String channelJson);

	Result<String> updateNetStatus(String machineCode, Integer netStatus);

	Result<List<String>> updateMachineListNetStatus(List<String> list, Integer netStatus);



}
