package com.inno72.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityInfoDesc;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityInfoDescMapper extends Mapper<Inno72ActivityInfoDesc> {
	List<Inno72ActivityInfoDesc> selectInfoDesc(@Param("merchantId") String merchantId,@Param("activityId") String activityId);
}