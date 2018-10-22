package com.inno72.app.service;

import java.util.List;
import java.util.Map;

import com.inno72.app.model.Inno72Machine;
import com.inno72.app.model.Inno72MachineBatch;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

	Result<String> generateMachineId(String deviceId, String batcId);

	Result<String> initMachine(Map<String, Object> msg);

	Result<String> updateMachineStatus(Map<String, Object> msg);

	Result<String> setMachineChannel(Map<String, Object> msg);

	Result<Integer> getMachineStatus(Map<String, Object> msg);

	Result<String> getMachineLocale(Map<String, Object> msg);

	Result<List<Inno72MachineBatch>> getMachineBatchs();

	Result<String> updateMachineCode(Map<String, Object> msg);

	Result<List<Map<String, Object>>> getMachineChannels(Map<String, Object> msg);

	Result<String> updateMachineChannels(Map<String, Object> msg);

	Result<String> resetTuMachine(Map<String, Object> msg);

}
