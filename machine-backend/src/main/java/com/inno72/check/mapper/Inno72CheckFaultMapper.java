package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckFaultMapper extends Mapper<Inno72CheckFault> {
	
	
	List<Inno72CheckFault> selectByPage(Map<String, Object> params);
	
}