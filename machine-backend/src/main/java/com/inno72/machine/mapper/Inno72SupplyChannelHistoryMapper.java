package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.SupplyOrderVo;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelHistoryMapper extends Mapper<Inno72SupplyChannelHistory> {

    List<Inno72SupplyChannelHistory> getSupplyOrderGoods(Map<String, Object> map);

}