package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72MachineEnter;
import com.inno72.Interact.vo.MachineEnterVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineEnterMapper extends Mapper<Inno72MachineEnter> {

	int insetMachineEnterList(@Param("list") List<Inno72MachineEnter> list);

	List<MachineEnterVo> selectByPage(Map<String, Object> params);

	List<MachineEnterVo> selectMachineEnterAll(Map<String, Object> params);

	List<Inno72MachineEnter> selectMachineEnterList(Map<String, Object> params);

	Map<String, String> selectMachineAddress(@Param("machineId") String machineId);
}