package com.inno72.app.service;

import java.util.List;

import com.inno72.app.model.Inno72SupplyChannel;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/10.
 */
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

	Result<String> initChannel(String machineId, String machineCode, List<Inno72SupplyChannel> channels);

	Result<String> updateChannel(String machineId, String machineCode, List<Inno72SupplyChannel> channels);

}
