package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Machine;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

    List<Inno72Machine> findMachineByMachineStatus(int machineStatus);


}