package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72ActivityPlan;
import com.inno72.vo.Inno72NoPlanInfoVo;

import java.util.List;


@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityPlanMapper extends Mapper<Inno72ActivityPlan> {

    List<Inno72NoPlanInfoVo> selectNoPlanedMachine(String taskTime);
}