package com.inno72.game.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.game.model.Inno72Game;

@org.apache.ibatis.annotations.Mapper
public interface Inno72GameMapper extends Mapper<Inno72Game> {
	
	
	
	int selectIsUseing(String id);
	List<Inno72Game> selectByPage(Map<String, Object> params);
	
	void deleteMachineGameByMachineId(@Param("mIds") List<String> mIds);
	
	
	void addMachineGame(@Param("machineGames") List<Map<String, Object>> machineGames);
	
	
}