package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Activity;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityMapper extends Mapper<Inno72Activity> {
	
	List<Inno72Activity> selectByPage(Map<String, Object> params);
	
	int selectIsUseing(String id);
}