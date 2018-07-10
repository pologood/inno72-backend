package com.inno72.machine.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannelHist;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelServiceHist extends Service<Inno72SupplyChannelHist> {

	Result subCount(Inno72SupplyChannelHist supplyChannel);

	Result getSupplyChannel(Inno72SupplyChannelHist supplyChannel);

	Result<String> init(String machineId, List<Inno72SupplyChannelHist> channels);

	Result<String> merge(Inno72SupplyChannelHist supplyChannel);

	Result<String> split(Inno72SupplyChannelHist supplyChannel);

	Result<String> clear(Inno72SupplyChannelHist supplyChannel);

	Result<String> downAll(Inno72SupplyChannelHist supplyChannel);

	Result<Map<String, Object>> history(Inno72SupplyChannelHist supplyChannel);

	List<Inno72SupplyChannelHist> getListForPage(Inno72SupplyChannelHist supplyChannel);

}
