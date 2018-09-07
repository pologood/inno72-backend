package com.inno72.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72User;

@org.apache.ibatis.annotations.Mapper
public interface Inno72UserMapper extends Mapper<Inno72User> {

	List<Inno72User> selectUsersByPage(Map<String, Object> param);

	List<String> selectUserFunction(@Param("userId") String userId);
}
