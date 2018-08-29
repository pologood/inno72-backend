package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72SupplyChannel;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {
	public void closeSupply(Inno72SupplyChannel supplyChannel);
}