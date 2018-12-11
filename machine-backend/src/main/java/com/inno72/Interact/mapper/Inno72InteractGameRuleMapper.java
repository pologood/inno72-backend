package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractGameRule;
import com.inno72.Interact.vo.InteractGameRuleVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractGameRuleMapper extends Mapper<Inno72InteractGameRule> {

	int insertInteractGameRuleList(@Param("list") List<Inno72InteractGameRule> list);

	List<InteractGameRuleVo> selectGameRule(Map<String, Object> pm);
}