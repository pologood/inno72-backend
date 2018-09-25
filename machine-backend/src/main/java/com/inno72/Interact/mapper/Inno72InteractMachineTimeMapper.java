package com.inno72.Interact.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractMachineTime;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMachineTimeMapper extends Mapper<Inno72InteractMachineTime> {

	int insertInteractMachineTimeList(@Param("list") List<Inno72InteractMachineTime> list);
}