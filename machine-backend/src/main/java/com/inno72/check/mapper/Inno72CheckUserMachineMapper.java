package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckUserMachineMapper extends Mapper<Inno72CheckUserMachine> {
	
	int deleteByUserId(@Param("userId")String userId);
	
	int insertUserMachineList(@Param("list") List<Inno72CheckUserMachine> list);
	
	List<Map<String, Object>> selectUserMachinDetailList(@Param("userId") String userId);
	
	
}