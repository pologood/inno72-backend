package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Channel;

public interface Inno72ChannelMapper extends Mapper<Inno72Channel> {
	
	List<Inno72Channel> selectByPage(Map<String, Object> params);

	int selectIsUseing(String id);
}