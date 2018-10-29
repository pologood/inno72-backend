package com.inno72.machine.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72MachineBatchDetail;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineBatchDetailMapper extends Mapper<Inno72MachineBatchDetail> {
	Inno72MachineBatchDetail selectByParam(Map<String,Object> map);
}