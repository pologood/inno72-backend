package com.inno72.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityShops;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityShopsMapper extends Mapper<Inno72ActivityShops> {

	int insertActivityShopsList(@Param("list") List<Inno72ActivityShops> list);

	int deleteByActivityId(@Param("activityId") String activityId);
}