package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Task;
import com.inno72.machine.vo.Inno72TaskVo;
import com.inno72.project.vo.Inno72AdminAreaVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72TaskMapper extends Mapper<Inno72Task> {

	List<Inno72AdminAreaVo> selectAreaMachineList(Map<String, Object> params);

	List<Inno72AdminAreaVo> selectMachineList(Map<String, Object> params);

	Inno72TaskVo selectTaskDetail(String id);

	List<Inno72TaskVo> selectByPage(Map<String, Object> params);

	List<Map<String, Object>> selectAppList(Map<String, Object> params);
}