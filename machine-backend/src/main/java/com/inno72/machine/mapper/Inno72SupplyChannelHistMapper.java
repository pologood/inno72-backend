package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelHist;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelHistMapper extends Mapper<Inno72SupplyChannelHist> {
	int subCount(Inno72SupplyChannelHist supplyChannel);

	Inno72SupplyChannelHist selectByParam(Map<String, Object> map);

	List<Inno72SupplyChannelHist> selectListByParam(Map<String, Object> map);

	int updateByParam(Inno72SupplyChannelHist supplyChannel);

	int updateChild(Inno72SupplyChannelHist upChildChannel);

	int updateListByParam(Inno72SupplyChannelHist supplyChannel);

	Inno72SupplyChannelHist selectByParentCode(Map<String, Object> map);

	List<Inno72SupplyChannelHist> selectListForPage(Inno72SupplyChannelHist supplyChannel);
}