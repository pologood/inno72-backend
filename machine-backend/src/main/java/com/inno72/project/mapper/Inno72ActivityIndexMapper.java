package com.inno72.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityIndex;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityIndexMapper extends Mapper<Inno72ActivityIndex> {
	List<Inno72ActivityIndex> selectIndex(@Param("merchantId") String merchantId,
			@Param("activityId") String activityId, @Param("activityIndexType") String activityIndexType);
}