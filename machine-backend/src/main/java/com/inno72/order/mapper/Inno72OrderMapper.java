package com.inno72.order.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelDict;
import com.inno72.order.model.Inno72Order;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72OrderMapper extends Mapper<Inno72Order> {
    List<Inno72Order> seleByParamForPage(Inno72Order order);
}