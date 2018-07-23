package com.inno72.machine.mapper;


import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Locale;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72LocaleMapper extends Mapper<Inno72Locale> {
	
	List<Inno72Locale> selectByAreaCode(String areaCode);


	List<Inno72Locale> selectByMall(String mall);
}