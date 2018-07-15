package com.inno72.project.mapper;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityPlanGoods;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanGoodsMapper extends Mapper<Inno72ActivityPlanGoods> {
	
	int deleteByPlanId(@Param("planId")String planId);
}