package com.inno72.merchant.service;
import com.inno72.common.Result;
import com.inno72.merchant.model.Inno72SupplyChannel;
import com.inno72.common.Service;
import com.inno72.merchant.vo.SupplyChannelVo;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

    Result subCount(Inno72SupplyChannel supplyChannel);

    Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

    Result init(String merchantId);

    Result merge(Inno72SupplyChannel supplyChannel);

    Result split(Inno72SupplyChannel supplyChannel);

    Result clear(Inno72SupplyChannel supplyChannel);
}