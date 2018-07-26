package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Machine;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

	List<Inno72Machine> selectMachinesByPage(Map<String, Object> param);

	List<String> findMachineByMachineCode(int machineStatus);



}