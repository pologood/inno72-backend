package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelHistoryMapper extends Mapper<Inno72SupplyChannelHistory> {

	List<Inno72SupplyChannelHistory> getSupplyOrderGoods(Map<String, Object> map);

	List<Map<String, Object>> selectDayGoodsCount(Map<String, Object> map);

	List<Map<String, Object>> selectDayGoodsList(Map<String, Object> map);

	List<Map<String, Object>> selectDayGoodsDetail(Map<String, Object> map);

}