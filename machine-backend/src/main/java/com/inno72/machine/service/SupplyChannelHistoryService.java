package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;

import java.util.List;

public interface SupplyChannelHistoryService extends Service<Inno72SupplyChannelHistory> {

    public List<SupplyOrderVo> list(SupplyOrderVo supplyOrderVo);

    Result<SupplyOrderVo> detail(SupplyRequestVo vo);
}
