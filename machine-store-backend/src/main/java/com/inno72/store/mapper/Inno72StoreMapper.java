package com.inno72.store.mapper;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.vo.StoreVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreMapper extends Mapper<Inno72Store> {

	StoreVo selectById(String id);
}