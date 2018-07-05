package com.inno72.service;
import com.inno72.common.Result;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

    Result subCount(Inno72SupplyChannel supplyChannel);

    Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

    Result init(String merchantId);
}
