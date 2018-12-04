package com.inno72.order.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.order.model.Inno72Order;

@org.apache.ibatis.annotations.Mapper
public interface Inno72OrderMapper extends Mapper<Inno72Order> {
	List<Inno72Order> seleByParamForPg(Inno72Order order);
}
