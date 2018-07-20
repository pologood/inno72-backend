package com.inno72.check.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckFaultTypeMapper extends Mapper<Inno72CheckFaultType> {
	
	List<Inno72CheckFaultType> selectByPage(@Param("keyword")String keyword);
	
}