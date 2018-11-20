package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72MachineLocaleDetail;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineLocaleDetailMapper extends Mapper<Inno72MachineLocaleDetail> {

	List<Map<String, Object>> selectMachineTimeByPage(Map<String, Object> map);

	List<Map<String, Object>> selectMachineLocaleDetail(Map<String, Object> map);
}