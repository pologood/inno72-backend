package com.inno72.store.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72StoreGoods;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreGoodsMapper extends Mapper<Inno72StoreGoods> {
	List<Inno72StoreGoods> selectByParam(Map<String,Object> map);
}