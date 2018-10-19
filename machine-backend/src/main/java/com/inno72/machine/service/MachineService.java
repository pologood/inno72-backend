package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72AppLog;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineExceptionVo;
import com.inno72.machine.vo.MachineListVo;
import com.inno72.machine.vo.MachineListVo1;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachinePortalVo;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.MachineStockOutInfo;
import com.inno72.machine.vo.PointLog;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.machine.vo.UpdateMachineVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

	Result<List<MachineNetInfo>> updateMachineListNetStatus(List<MachineNetInfo> list);

	Result<List<MachineListVo>> findMachines(String machineCode, String localCode, String startTime, String endTime,
			String machineType, String machineStatus);

	Result<String> updateLocale(String id, String localeId, String address);

	Result<List<ChannelListVo>> channelList(String id);

	Result<String> deleteChannel(List<UpdateMachineChannelVo> channels);

	Result<String> updateGoodsCount(List<UpdateMachineChannelVo> channels);

	Result<MachineStatusVo> machineStatus(String machineId);

	Result<MachineAppStatus> appStatus(String machineId);

	Result<String> updateInfo(String machineId, Integer updateStatus);

	Result<String> cutApp(String machineId, String appPackageName);

	Result<String> installApp(String machineId, String appPackageName, String url, Integer versionCode);

	Result<List<String>> findMachineByMachineStatus(int id);

	List<MachineListVo1> findMachinePlan(String machineCode, String state, String localCode, String startTime,
			String endTime);

	Result<MachinePortalVo> findMachinePortalData();

	Result<List<MachineExceptionVo>> findExceptionMachine(Integer type);

	Result<List<MachineStockOutInfo>> findMachineStockoutInfo(String machineId);

	Result<String> updateMachine(UpdateMachineVo vo);

	Result<String> updateMachineCode(String machineId, String machineCode);

	Result<Inno72Machine> findMachineInfoById(String machineId);

	Result<String> cutDesktop(String machineId, Integer status);

	Result<List<PointLog>> machinePointLog(String machineCode, String startTime, String endTime);

	Result<String> updateTemperature(String machineId, Integer temperature);

	Result<String> findTemperature(String machineId);

	Result<String> grabLog(String machineId, Integer logType, String startTime, String endTime);

	Result<List<Inno72AppLog>> getLogs(String machineId);
}
