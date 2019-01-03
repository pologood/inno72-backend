package com.inno72.store.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72Storekeeper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StorekeeperMapper extends Mapper<Inno72Storekeeper> {
	Inno72Storekeeper selectOneByParam(Map<String,Object> map);

	Inno72Storekeeper selectKepperModel(Map<String,Object> map);

	List<Inno72Storekeeper> selectByPageLevel(Map<String,Object> map);

	Inno72Storekeeper selectDetail(String id);
}