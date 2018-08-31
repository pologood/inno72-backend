package com.inno72.machine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72TaskMachine;
import com.inno72.machine.vo.Inno72TaskMachineVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72TaskMachineMapper extends Mapper<Inno72TaskMachine> {

	int deleteByTaskId(@Param("taskId") String taskId);

	int insertTaskMachineList(@Param("list") List<Inno72TaskMachineVo> list);
}