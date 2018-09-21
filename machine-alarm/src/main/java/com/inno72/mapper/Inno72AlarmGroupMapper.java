package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AlarmGroup;
@org.apache.ibatis.annotations.Mapper
public interface Inno72AlarmGroupMapper extends Mapper<Inno72AlarmGroup> {
	Inno72AlarmGroup selectByParam(Map<String,Object> map);
}
