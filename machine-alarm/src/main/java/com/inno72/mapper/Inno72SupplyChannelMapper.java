package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72SupplyChannel;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {
	public void closeSupply(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> selectNormalSupply(Map<String,Object> map);

	List<Inno72SupplyChannel> selectByParam(Map<String,Object> map);
}