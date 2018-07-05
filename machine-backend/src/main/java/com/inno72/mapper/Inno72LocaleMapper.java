package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Locale;
import com.inno72.vo.Inno72LocaleVo;

public interface Inno72LocaleMapper extends Mapper<Inno72Locale> {
	
	Inno72LocaleVo selectById(String id);
	
	int selectIsUseing(String id);
	
	List<Inno72LocaleVo> selectByPage(Map<String, Object> params);
}