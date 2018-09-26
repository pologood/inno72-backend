package com.inno72.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.app.model.Inno72App;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AppMapper extends Mapper<Inno72App> {

	public List<Inno72App> selectList(@Param("appPackageNames") String[] appPackageNames);

	public Inno72App findOneByParam(Map<String,Object> map);
}