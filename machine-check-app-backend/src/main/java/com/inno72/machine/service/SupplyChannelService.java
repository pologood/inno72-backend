package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Goods;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.vo.WorkOrderVo;

import java.util.List;
import java.util.Map;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@SuppressWarnings("rawtypes")
public interface SupplyChannelService extends Service<Inno72SupplyChannel> {

	Result subCount(Inno72SupplyChannel supplyChannel);

	Result getSupplyChannel(Inno72SupplyChannel supplyChannel);

	Result<String> init(String machineId, List<Inno72SupplyChannel> channels);

	Result<String> merge(Inno72SupplyChannel supplyChannel);

	Result<String> split(Inno72SupplyChannel supplyChannel);

	Result<String> clear(Inno72SupplyChannel supplyChannel);

	Result<String> downAll(Inno72SupplyChannel supplyChannel);

	Result<Map<String, Object>> history(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> getListForPage(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> getList(String machineId);

    List<Inno72Machine> getMachineLackGoods(String checkUserId);

	List<Inno72Goods> getGoodsLack(String checkUserId);

	List<Inno72Machine> getMachineByLackGoods(String checkUserId,String goodsId);

	List<Inno72Goods> getGoodsByMachineId(String machineId);

    Result<String> clearAll(String machineId);

	Result<String> supplyAll(String machineId);

	Result<String> submit(List<Inno72SupplyChannel> supplyChannelList);

	Result<List<WorkOrderVo>> workOrderList(String checkUserId);
}
