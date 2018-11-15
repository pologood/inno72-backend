package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Machine;

/**
 * @author
 */
@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

    /**
     * find machine
     *
     * @param :machineStatus
     * @return :List<Inno72Machine>
     */
    List<Inno72Machine> findMachineByMachineStatus(int machineStatus);

    /**
     * find machine
     *
     * @param :machineCode
     * @return :Inno72Machine
     */
    Inno72Machine findMachineByMachineCode(String machineCode);

    /**
     * find machineList
     *
     * @param :machineList
     * @return :List<Inno72Machine>
     */
    List<Inno72Machine> findMachineNetStatusOpenList(List<String> machineList);

    List<Inno72Machine> findAlarmAllMachine();

	List<Inno72Machine> selectSignMachineList(String findTime);
}