package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.vo.Inno72InteractVo;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMapper extends Mapper<Inno72Interact> {

	List<InteractListVo> selectByPage(Map<String, Object> params);

	Inno72InteractVo selectInteractDetail(@Param("id") String id);
}