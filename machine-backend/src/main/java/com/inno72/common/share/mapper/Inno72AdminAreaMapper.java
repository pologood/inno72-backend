package com.inno72.common.share.mapper;

import com.inno72.common.Mapper;
import com.inno72.common.share.model.Inno72AdminArea;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AdminAreaMapper extends Mapper<Inno72AdminArea> {
	
	Inno72AdminArea selectByCode(String code);
}