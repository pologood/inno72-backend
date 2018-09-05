package com.inno72.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72FunctionData;

@org.apache.ibatis.annotations.Mapper
public interface Inno72FunctionDataMapper extends Mapper<Inno72FunctionData> {

	List<Inno72FunctionData> findFunctionDataByParentId(@Param("parentId") String parentId);

	List<Inno72FunctionData> selectUserFunctionDataList(@Param("userId") String userId);
}