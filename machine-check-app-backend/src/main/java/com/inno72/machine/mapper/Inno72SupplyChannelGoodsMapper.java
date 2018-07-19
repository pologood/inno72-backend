package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelGoods;

import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelGoodsMapper extends Mapper<Inno72SupplyChannelGoods> {

    int deleteBySupplyChannelIds(String[] supplyChannelIds);
}