package com.inno72.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72UserFunctionArea;

@org.apache.ibatis.annotations.Mapper
public interface Inno72UserFunctionAreaMapper extends Mapper<Inno72UserFunctionArea> {

	int insertUserFunctionAreaList(@Param("list") List<Inno72UserFunctionArea> list);
}