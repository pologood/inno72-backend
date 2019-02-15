package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72CheckGoodsDetail;
import com.inno72.machine.model.Inno72CheckGoodsNum;
import com.inno72.machine.vo.SupplyDataVo;

public interface SupplyDataService extends Service<Inno72CheckGoodsNum> {

	List<Inno72CheckGoodsNum> findListByPage(SupplyDataVo supplyDataVo);

	Result<List<Inno72CheckGoodsDetail>> findDetail(String id);
}
