package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Game;
import com.inno72.project.vo.Inno72GameVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GameMapper extends Mapper<Inno72Game> {

	int selectIsUseing(String id);

	List<Inno72GameVo> selectByPage(Map<String, Object> params);

	void deleteMachineGameByMachineId(@Param("mIds") List<String> mIds);

	void addMachineGame(@Param("machineGames") List<Map<String, Object>> machineGames);

}