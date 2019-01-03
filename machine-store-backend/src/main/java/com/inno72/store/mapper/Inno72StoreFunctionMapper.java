package com.inno72.store.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72StoreFunction;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreFunctionMapper extends Mapper<Inno72StoreFunction> {
	List<Inno72StoreFunction> selectAllFunction();
}