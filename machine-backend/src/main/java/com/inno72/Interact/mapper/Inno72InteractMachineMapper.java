package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMachineMapper extends Mapper<Inno72InteractMachine> {

	List<MachineVo> selectPlanMachines(Map<String, Object> pm);

	List<MachineVo> selectInteractMachines(Map<String, Object> pm);

	List<MachineVo> getHavingMachines(Map<String, Object> pm);

	int insertInteractMachineList(@Param("list") List<Inno72InteractMachine> list);

}