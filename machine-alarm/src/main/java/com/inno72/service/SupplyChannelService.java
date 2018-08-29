package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72SupplyChannel;

public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

    public void closeSupply(Inno72SupplyChannel supplyChannel);
}
