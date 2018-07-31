package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Machine;
import com.inno72.model.MachineLogInfo;

import java.util.List;

/**
 * @author
 */
@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

    /**
     * find machineList
     *
     * @param :machineList
     * @return :List<MachineLogInfo>
     */
    List<Inno72Machine> findMachineNetStatusOpenList(List<String> machineList);


}