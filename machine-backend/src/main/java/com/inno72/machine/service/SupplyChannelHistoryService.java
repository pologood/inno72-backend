package com.inno72.machine.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;

public interface SupplyChannelHistoryService extends Service<Inno72SupplyChannelHistory> {

	public List<SupplyOrderVo> findListByPage(SupplyOrderVo supplyOrderVo);

	Result<List<Inno72SupplyChannelHistory>> detail(SupplyRequestVo vo);

	List<Map<String, Object>> dayGoodsCount(SupplyOrderVo supplyOrderVo);

	List<Map<String, Object>> dayGoodsList(SupplyOrderVo supplyOrderVo);

	List<Map<String, Object>> dayGoodsDetail(String machineId, String dateTime);
}
