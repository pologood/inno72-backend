package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.UpdateMachineChannelVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MachineService extends Service<Inno72Machine> {

	Result<String> initMachine(String deviceId, String channelJson);

	Result<String> updateNetStatus(String machineCode, Integer netStatus);

	Result<List<String>> updateMachineListNetStatus(List<String> list, Integer netStatus);

	Result<List<Inno72Machine>> findMachines(String machineCode, String localCode);

	Result<String> updateLocale(String id, String localeId, String address);

	Result<List<ChannelListVo>> channelList(String id);

	Result<String> deleteChannel(List<UpdateMachineChannelVo> channels);

	Result<String> updateGoodsCount(List<UpdateMachineChannelVo> channels);

	Result<MachineStatusVo> machineStatus(String machineId);

	Result<MachineAppStatus> appStatus(String machineId);

	Result<String> updateInfo(String machineId, Integer updateStatus);

}
