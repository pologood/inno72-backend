package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Activity;
import com.inno72.project.vo.Inno72ActivityVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityMapper extends Mapper<Inno72Activity> {
	
	List<Inno72ActivityVo> selectByPage(Map<String, Object> params);
	
	int selectIsUseing(String id);
	
	Inno72ActivityVo selectById(String id);
}