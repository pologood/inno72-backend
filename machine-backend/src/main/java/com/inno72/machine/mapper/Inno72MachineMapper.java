package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.vo.MachineExceptionVo;
import com.inno72.machine.vo.MachineListVo;
import com.inno72.machine.vo.MachineListVo1;
import com.inno72.machine.vo.MachineStockOutInfo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

	List<MachineListVo> selectMachinesByPage(Map<String, Object> param);

	List<MachineListVo1> findMachinePlan(Map<String, Object> param);

	List<String> findMachineByMachineStatus(int machineStatus);

	List<MachineExceptionVo> findStockOutMachines();

	List<MachineExceptionVo> findMachines(Map<String, Object> param);

	List<MachineStockOutInfo> findMachineStockoutInfo(String machineId);

	Inno72Machine findMachineInfoById(String machineId);

	List<MachineExceptionVo> findMachineLocked();

}