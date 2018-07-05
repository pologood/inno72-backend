package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannel;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

	Result subCount(Inno72SupplyChannel supplyChannel);

	Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

	Result<String> init(String machineId);

	Result<String> merge(Inno72SupplyChannel supplyChannel);

	Result<String> split(Inno72SupplyChannel supplyChannel);

	Result<String> clear(Inno72SupplyChannel supplyChannel);

    Result<String> downAll(Inno72SupplyChannel supplyChannel);
}
