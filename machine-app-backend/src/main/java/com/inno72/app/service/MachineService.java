package com.inno72.app.service;

import java.util.Map;

import com.inno72.app.model.Inno72Machine;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

	Result<String> generateMachineId(String deviceId);

	Result<String> initMachine(Map<String, Object> msg);

	Result<String> updateMachineStatus(Map<String, Object> msg);

	Result<String> setMachineChannel(Map<String, Object> msg);

}
