package com.inno72.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityPlanGameResult;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanGameResultMapper extends Mapper<Inno72ActivityPlanGameResult> {
	
	int deleteByPlanId(@Param("planId")String planId);
	
	int insertActivityPlanGameResultList(@Param("list") List<Inno72ActivityPlanGameResult> list);
}