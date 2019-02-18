package com.inno72.store.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.vo.StoreVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreMapper extends Mapper<Inno72Store> {

	StoreVo selectById(String id);

	List<StoreVo> selectByPage(Map<String, Object> map);
}