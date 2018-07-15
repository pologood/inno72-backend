package com.inno72.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityPlanMachine;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanMachineMapper extends Mapper<Inno72ActivityPlanMachine> {
	
	int insertActivityPlanMachineList(@Param("list") List<Inno72ActivityPlanMachine> list);
}