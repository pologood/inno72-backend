package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.model.GoodsBean;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {
	int subCount(Inno72SupplyChannel supplyChannel);

	Inno72SupplyChannel selectByParam(Map<String, Object> map);

	List<Inno72SupplyChannel> selectListByParam(Map<String, Object> map);

	int updateByParam(Inno72SupplyChannel supplyChannel);

	int updateListByParam(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> selectTask();

	List<Inno72SupplyChannel> selectExceptionList(String machineId);

	int updateOpen(Map<String,Object> map);

	List<Inno72SupplyChannel> selectSupplyAndGoods(@Param("suuplyChannelArray") String[] suuplyChannelArray);

	List<Inno72SupplyChannel> selectAllSupply(String machineId);

	Inno72SupplyChannel selectOneBy(Map<String,Object> map);
}