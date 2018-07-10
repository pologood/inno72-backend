package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Channel;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ChannelMapper extends Mapper<Inno72Channel> {
	
	List<Inno72Channel> selectByPage(Map<String, Object> params);

	int selectIsUseing(String id);
	
	int getCount(@Param("code") String code);
}