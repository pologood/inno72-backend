package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannel;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {
	int subCount(Inno72SupplyChannel supplyChannel);

	Inno72SupplyChannel selectByParam(Map<String,Object> map);

	List<Inno72SupplyChannel> selectListByParam(Map<String,Object> map);

	int updateByParam(Inno72SupplyChannel supplyChannel);

	int updateChild(Inno72SupplyChannel upChildChannel);
}