package com.inno72.store.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72StoreOrder;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreOrderMapper extends Mapper<Inno72StoreOrder> {
	List<Inno72StoreOrder> selectOrderByPage(Map<String,Object> map);

	Inno72StoreOrder selectDetailById(String id);
}