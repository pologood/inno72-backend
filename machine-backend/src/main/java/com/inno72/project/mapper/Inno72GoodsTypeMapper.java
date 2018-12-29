package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72GoodsType;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GoodsTypeMapper extends Mapper<Inno72GoodsType> {

	List<Inno72GoodsType> selectByPage(Map<String, Object> params);
}