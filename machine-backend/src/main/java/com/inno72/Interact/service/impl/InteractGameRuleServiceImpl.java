package com.inno72.Interact.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractGameRuleMapper;
import com.inno72.Interact.model.Inno72InteractGameRule;
import com.inno72.Interact.service.InteractGameRuleService;
import com.inno72.Interact.vo.InteractGameRuleVo;
import com.inno72.common.AbstractService;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
@Service
@Transactional
public class InteractGameRuleServiceImpl extends AbstractService<Inno72InteractGameRule>
		implements InteractGameRuleService {

	private static Logger logger = LoggerFactory.getLogger(InteractGameRuleServiceImpl.class);
	@Resource
	private Inno72InteractGameRuleMapper inno72InteractGameRuleMapper;

	@Override
	public List<InteractGameRuleVo> getGameRuleList(String interactId) {
		logger.info("---------------------获取游戏活动掉货规则-------------------");

		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);

		return inno72InteractGameRuleMapper.selectGameRule(pm);

	}

}
