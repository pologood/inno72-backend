package com.inno72.order.mapper;

import com.inno72.common.Mapper;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.model.Inno72OrderGoods;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72OrderGoodsMapper extends Mapper<Inno72OrderGoods> {

    List<Inno72OrderGoods> seleByParam(Inno72OrderGoods orderGoods);
}