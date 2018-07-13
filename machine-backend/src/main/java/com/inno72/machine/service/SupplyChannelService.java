package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.UpdateMachineChannelVo;

/**
 * Created by CodeGenerator on 2018/07/10.
 */
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

	Result<String> initChannel(String machineCode, List<Inno72SupplyChannel> channels);

	Result<List<ChannelListVo>> findChannelListByMachineId(String id);

	Result<String> deleteChannel(List<UpdateMachineChannelVo> channels);

	Result<String> updateGoodsCount(List<UpdateMachineChannelVo> channels);

}
