package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Shops;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ShopsMapper extends Mapper<Inno72Shops> {
	
	List<Inno72Shops> selectByPage(Map<String, Object> params);
	
	int selectIsUseing(String id);
}