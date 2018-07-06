package com.inno72.machine.mapper;


import java.util.List;
import java.util.Map;
import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.vo.Inno72LocaleVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72LocaleMapper extends Mapper<Inno72Locale> {
	
	Inno72LocaleVo selectById(String id);
	
	int selectIsUseing(String id);
	
	List<Inno72LocaleVo> selectByPage(Map<String, Object> params);
}