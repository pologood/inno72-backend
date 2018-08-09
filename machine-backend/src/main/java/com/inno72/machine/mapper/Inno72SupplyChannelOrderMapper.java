package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelOrder;
import com.inno72.machine.vo.SupplyOrderVo;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelOrderMapper extends Mapper<Inno72SupplyChannelOrder> {

	List<SupplyOrderVo> getOrderList(Map<String,Object> map);
}