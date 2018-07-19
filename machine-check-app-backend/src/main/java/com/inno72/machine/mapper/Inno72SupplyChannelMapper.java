package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannel;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {
	int subCount(Inno72SupplyChannel supplyChannel);

	Inno72SupplyChannel selectByParam(Map<String, Object> map);

	List<Inno72SupplyChannel> selectListByParam(Map<String, Object> map);

	int updateByParam(Inno72SupplyChannel supplyChannel);

	int updateChild(Inno72SupplyChannel upChildChannel);

	int updateListByParam(Inno72SupplyChannel supplyChannel);

	Inno72SupplyChannel selectByParentCode(Map<String, Object> map);

	List<Inno72SupplyChannel> selectListForPage(Inno72SupplyChannel supplyChannel);
}