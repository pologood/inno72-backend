package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.MachineGoodsCount;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelHistoryMapper extends Mapper<Inno72SupplyChannelHistory> {

	List<Inno72SupplyChannelHistory> getSupplyOrderGoods(Map<String, Object> map);

	List<Map<String, Object>> selectDayGoodsCountByPage(Map<String, Object> map);

	List<MachineGoodsCount> selectDayGoodsCountExcel(Map<String, Object> map);
	
	List<Map<String, Object>> selectDayGoodsListByPage(Map<String, Object> map);

	List<Map<String, Object>> selectDayGoodsDetail(Map<String, Object> map);

}