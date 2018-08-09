package com.inno72.machine.service;

import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.SupplyOrderVo;

import java.util.List;

public interface SupplyChannelHistoryService extends Service<Inno72SupplyChannelHistory> {

    public List<SupplyOrderVo> list(SupplyOrderVo supplyOrderVo);

}
