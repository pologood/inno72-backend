package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72AdminArea;

import java.util.List;


@org.apache.ibatis.annotations.Mapper
public interface Inno72AdminAreaMapper extends Mapper<Inno72AdminArea> {
	
	public List<Inno72AdminArea> selectFistLevelArea();

	public Inno72AdminArea cityLevelArea(String code);

}