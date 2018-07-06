package com.inno72.system.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72Function;

@org.apache.ibatis.annotations.Mapper
public interface Inno72FunctionMapper extends Mapper<Inno72Function> {

	List<Inno72Function> findFunctionsByUserId(String id);

}