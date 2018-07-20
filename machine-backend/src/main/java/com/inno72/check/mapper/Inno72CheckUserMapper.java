package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Mapper;
import com.inno72.project.vo.Inno72AdminAreaVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckUserMapper extends Mapper<Inno72CheckUser> {
	
	List<Inno72CheckUser> selectByPage(@Param("keyword")String keyword);
	
	Inno72CheckUserVo selectUserDetail(String id);
	
	List<Inno72AdminAreaVo> selectAreaMachineList(Map<String, Object> params);
	
	List<Inno72AdminAreaVo> selectMachineList(Map<String, Object> params);
}