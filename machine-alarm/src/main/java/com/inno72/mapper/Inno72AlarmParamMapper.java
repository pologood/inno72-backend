package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AlarmParam;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AlarmParamMapper extends Mapper<Inno72AlarmParam> {


	public Inno72AlarmParam selectByParam(Map<String,Object> map);
}
