package com.inno72.goods.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.goods.model.Inno72Goods;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GoodsMapper extends Mapper<Inno72Goods> {
	
	int selectIsUseing(String id);
	
	int selectIsUseing1(String id);
	
	int getCount(@Param("code") String code);
	
	List<Inno72Goods> selectByPage(Map<String, Object> params);
}