package com.inno72.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.model.Inno72SupplyChannel;

public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

    public void closeSupply(Inno72SupplyChannel supplyChannel);

    public List<Inno72SupplyChannel> selectNormalSupply(String machineId,String code);

	Inno72SupplyChannel selectByParam(String machineId, String channelNum);
}
