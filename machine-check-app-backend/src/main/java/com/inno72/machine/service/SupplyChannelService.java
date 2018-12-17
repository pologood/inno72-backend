package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.vo.CommonVo;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.machine.vo.WorkOrderVo;

import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@SuppressWarnings("rawtypes")
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

	Result<String> merge(Inno72SupplyChannel supplyChannel);

	Result<String> split(Inno72SupplyChannel supplyChannel);

	Result<String> clear(Inno72SupplyChannel supplyChannel);

	Result<Map<String, Object>> history(Inno72SupplyChannel supplyChannel);

	Result<List<Inno72SupplyChannel>> getList(String machineId);

    Result<List<Inno72Machine>> getMachineLackGoods(CommonVo commonVo);

	Result<List<Inno72Goods>> getGoodsLack(CommonVo commonVo);

	Result<List<Inno72Machine>> getMachineByLackGoods(String goodsId);

	Result<List<Inno72Goods>> getGoodsByMachineId(String machineId);

    Result<String> clearAll(String machineId);

	Result<String> supplyAll(String machineId);

	Result<String> submit(List<Map<String,Object>> mapList);

	List<WorkOrderVo> findByPage(String keyword,String findTime);

    Result<List<WorkOrderVo>> workOrderDetail(SupplyRequestVo vo);

    void findAndPushByTaskParam();

	Result<List<WorkOrderVo>> findOrderByMonth(SupplyRequestVo vo);

	Result<List<Inno72SupplyChannel>> exceptionList(SupplyRequestVo vo);

	Result<String> openSupplyChannel(SupplyRequestVo vo);

	Result<String> updateSupplyChannel(Map<String,Object> map);
}
