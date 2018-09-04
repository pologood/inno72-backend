package com.inno72.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72UserFunctionData;

@org.apache.ibatis.annotations.Mapper
public interface Inno72UserFunctionDataMapper extends Mapper<Inno72UserFunctionData> {

	int insertUserFunctionDataList(@Param("list") List<Inno72UserFunctionData> list);
}